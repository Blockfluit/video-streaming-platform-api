package nl.nielsvanbruggen.videostreamingplatform.stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.MediaRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.SubtitleRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.VideoRepository;
import nl.nielsvanbruggen.videostreamingplatform.global.util.MimeTypeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Service
@RequiredArgsConstructor
public class StreamService {
    private final static int MAX_CHUNK_SIZE_BYTES = 1024 * 1024;
    private final VideoRepository videoRepository;
    private final MediaRepository mediaRepository;
    private final SubtitleRepository subtitleRepository;
    @Value("${env.thumbnail.root}")
    private String thumbnailRoot;
    @Value("${env.snapshot.root}")
    private String snapshotRoot;
    @Value("${env.videos.root}")
    private String videosRoot;
    // Allows for storing paths to id in memory. Dramatically reduces the amount of database queries.
    private static final ConcurrentLinkedQueue<IdPath> thumbnailPaths = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<IdPath> snapshotPaths = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<IdPath> videoPaths = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<IdPath> subtitlePaths = new ConcurrentLinkedQueue<>();

    public ResponseEntity<?> getVideo(long id, HttpHeaders headers) {
        String path =  videoPaths.stream()
                .filter(entry -> entry.getId() == id)
                .map(IdPath::getPath)
                .findFirst()
                .orElseGet(() -> {
                    String relativePath = videoRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Video id does not exist.")).getPath();
                    IdPath idPath = new IdPath(id, relativePath);
                    if(videoPaths.size() < 1000) videoPaths.poll();
                    videoPaths.add(idPath);
                    return idPath.getPath();
                });
        String absolutePath = videosRoot + "/" + path;

        return createStreamResponseEntity(Path.of(absolutePath), headers);
    }

    public ResponseEntity<?> getSubtitle(long id) {
        String path =  subtitlePaths.stream()
                .filter(entry -> entry.getId() == id)
                .findFirst()
                .map(IdPath::getPath)
                .orElseGet(() -> {
                    String relativePath = subtitleRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Subtitle id does not exist.")).getPath();
                    IdPath idPath = new IdPath(id, relativePath);
                    if(subtitlePaths.size() < 300) subtitlePaths.poll();
                    subtitlePaths.add(idPath);
                    return idPath.getPath();
                });
        String absolutePath = videosRoot + "/" + path;

        return createCompleteResponseEntity(Path.of(absolutePath));
    }

    public ResponseEntity<?> getThumbnail(long id) {
        String path =  thumbnailPaths.stream()
                .filter(entry -> entry.getId() == id)
                .findFirst()
                .map(IdPath::getPath)
                .orElseGet(() -> {
                    String relativePath = mediaRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Media id does not exist.")).getThumbnail();
                    IdPath idPath = new IdPath(id, relativePath);
                    if(thumbnailPaths.size() < 2000) thumbnailPaths.poll();
                    thumbnailPaths.add(idPath);
                    return idPath.getPath();
                });
        String absolutePath = thumbnailRoot + "/" + path;

        return createCompleteResponseEntity(Path.of(absolutePath));
    }

    public ResponseEntity<?> getSnapshot(long id) {
        String path = snapshotPaths.stream()
                .filter(entry -> entry.getId() == id)
                .findFirst()
                .map(IdPath::getPath)
                .orElseGet(() -> {
                    String relativePath = videoRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Video id does not exist."))
                            .getSnapshot();
                    IdPath idPath = new IdPath(id, relativePath);
                    if(snapshotPaths.size() < 4000) snapshotPaths.poll();
                    snapshotPaths.add(idPath);
                    return idPath.getPath();
                });
        String absolutePath = snapshotRoot + "/" + path;

        return createCompleteResponseEntity(Path.of(absolutePath));
    }

    private ResponseEntity<?> createStreamResponseEntity(Path path, HttpHeaders headers) {
        long tot = totalBytes(path);

        if(headers.getRange().size() == 0) {
            System.out.println(path.getFileName());
            return createInitialResponse(path, tot);
        }

        String rangeHeader = headers.getRange().get(0).toString();
        long start = Long.parseLong(rangeHeader.split("-")[0]);
        long end = rangeHeader.split("-").length == 1 ?
                Math.min((start + MAX_CHUNK_SIZE_BYTES), tot - 1):
                Long.parseLong(rangeHeader.split("-")[1]);

        return createPartialResponse(path, start, end, tot);
    }

    private ResponseEntity<byte[]> createCompleteResponseEntity(Path path) {
        long tot = totalBytes(path);
        byte[] bytes = readBytes(path, 0, tot);

        MultiValueMap<String, String> responseHeaders = new HttpHeaders();
        responseHeaders.add("Accept-Ranges", "bytes");
        responseHeaders.add("Content-Type", MimeTypeUtil.getMimeType(path));
        responseHeaders.add("Content-Length", Long.toString(tot));
        responseHeaders.add("Cache-Control", "public, max-age=31536000");

        return new ResponseEntity<>(bytes, responseHeaders, HttpStatus.OK);
    }

    private ResponseEntity<?> createInitialResponse(Path path, long tot) {
        MultiValueMap<String, String> responseHeaders = new HttpHeaders();
        responseHeaders.add("Accept-Ranges", "bytes");
        responseHeaders.add("Content-Type", MimeTypeUtil.getMimeType(path));
        responseHeaders.add("Content-Length", Long.toString(tot));
        responseHeaders.add("Cache-Control", "no-cache");

        return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
    }

    private ResponseEntity<byte[]> createPartialResponse(Path path, long start, long end, long tot) {
        byte[] bytes = readBytes(path, start, end);

        MultiValueMap<String, String> responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Range", String.format("bytes %1$d-%2$d/%3$d", start, end, tot));
        responseHeaders.add("Accept-Ranges", "bytes");
        responseHeaders.add("Content-Type", MimeTypeUtil.getMimeType(path));
        responseHeaders.add("Content-Length", Long.toString(bytes.length));
        responseHeaders.add("Cache-Control", "public, max-age=3600");

        return new ResponseEntity<>(bytes, responseHeaders, HttpStatus.PARTIAL_CONTENT);
    }

    private byte[] readBytes(Path path, long start, long end) {
        try(InputStream is = Files.newInputStream(path)) {
            is.skip(start);
            // plus 1 because its zero-based
            return is.readNBytes((int) (end - start + 1));
        } catch (IOException e) {
            log.warn(e.getMessage());
            return new byte[0];
        }
    }
    private long totalBytes(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            log.warn(e.getMessage());
            return 0;
        }
    }
}
