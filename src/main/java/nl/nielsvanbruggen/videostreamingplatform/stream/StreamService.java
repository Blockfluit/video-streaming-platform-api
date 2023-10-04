package nl.nielsvanbruggen.videostreamingplatform.stream;

import lombok.RequiredArgsConstructor;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
    private static final Queue<IdPath> thumbnailPaths = new ArrayDeque<>(1000);
    private static final Queue<IdPath> snapshotPaths = new ArrayDeque<>(3000);
    private static final Queue<IdPath> videoPaths = new ArrayDeque<>(300);
    private static final Queue<IdPath> subtitlePaths = new ArrayDeque<>(300);

    public ResponseEntity<byte[]> getVideo(long id, HttpHeaders headers) {
        String path =  videoPaths.stream()
                .filter(entry -> entry.getId() == id)
                .map(IdPath::getPath)
                .findFirst()
                .orElseGet(() -> {
                    String relativePath = videoRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Video id does not exist.")).getPath();
                    IdPath idPath = new IdPath(id, relativePath);
                    videoPaths.add(idPath);
                    return idPath.getPath();
                });
        String absolutePath = videosRoot + "/" + path;

        return createStreamResponseEntity(absolutePath, headers);
    }

    public ResponseEntity<byte[]> getSubtitle(long id, HttpHeaders headers) {
        String path =  subtitlePaths.stream()
                .filter(entry -> entry.getId() == id)
                .findFirst()
                .map(IdPath::getPath)
                .orElseGet(() -> {
                    String relativePath = subtitleRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Subtitle id does not exist.")).getPath();
                    IdPath idPath = new IdPath(id, relativePath);
                    subtitlePaths.add(idPath);
                    return idPath.getPath();
                });
        String absolutePath = videosRoot + "/" + path;

        return createStreamResponseEntity(absolutePath, headers);
    }

    public ResponseEntity<byte[]> getThumbnail(long id, HttpHeaders headers) {
        String path =  thumbnailPaths.stream()
                .filter(entry -> entry.getId() == id)
                .findFirst()
                .map(IdPath::getPath)
                .orElseGet(() -> {
                    String relativePath = mediaRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Media id does not exist.")).getThumbnail();
                    IdPath idPath = new IdPath(id, relativePath);
                    thumbnailPaths.add(idPath);
                    return idPath.getPath();
                });
        String absolutePath = thumbnailRoot + "/" + path;

        return createStreamResponseEntity(absolutePath, headers);
    }

    public ResponseEntity<byte[]> getSnapshot(long id, HttpHeaders headers) {
        String path = snapshotPaths.stream()
                .filter(entry -> entry.getId() == id)
                .findFirst()
                .map(IdPath::getPath)
                .orElseGet(() -> {
                    String relativePath = videoRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Media id does not exist.")).getSnapshot();
                    IdPath idPath = new IdPath(id, relativePath);
                    snapshotPaths.add(idPath);
                    return idPath.getPath();
                });
        String absolutePath = snapshotRoot + "/" + path;

        return createStreamResponseEntity(absolutePath, headers);
    }

    private ResponseEntity<byte[]> createStreamResponseEntity(String filepath, HttpHeaders headers) {
        if(headers.getRange().size() == 0) {
            return createInitialResponse(filepath);
        }
        int start = Integer.parseInt(headers.getRange().get(0).toString()
                .split("-")[0]);
        return createPartialResponse(filepath, start);
    }

    private ResponseEntity<byte[]> createInitialResponse(String path) {
        int tot = totalBytes(path);
        // -1 because its zero-indexed
        int end = Math.min(MAX_CHUNK_SIZE_BYTES, tot) - 1;
        byte[] bytes = readBytes(path, 0);
        MultiValueMap<String, String> responseHeaders = createResponseHeaders(0, end, tot, bytes.length, MimeTypeUtil.getMimeType(path));

        return new ResponseEntity<>(bytes, responseHeaders, HttpStatus.OK);
    }

    private ResponseEntity<byte[]> createPartialResponse(String path, int off) {
        int tot = totalBytes(path);
        // -1 because its zero-indexed
        int end = Math.min((off + MAX_CHUNK_SIZE_BYTES), tot) - 1;
        byte[] bytes = readBytes(path, off);
        MultiValueMap<String, String> responseHeaders = createResponseHeaders(off, end, tot, bytes.length, MimeTypeUtil.getMimeType(path));

        return new ResponseEntity<>(bytes, responseHeaders, HttpStatus.PARTIAL_CONTENT);
    }

    private MultiValueMap<String, String> createResponseHeaders(int off, int end, int tot, int len, String mimeType) {
        MultiValueMap<String, String> responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Range", String.format("bytes %1$d-%2$d/%3$d", off, end, tot));
        responseHeaders.add("Accept-Ranges", "bytes");
        responseHeaders.add("Content-Type", mimeType);
        responseHeaders.add("Content-Length", Integer.toString(len));
        return responseHeaders;
    }

    private byte[] readBytes(String path, int off) {
        try(FileInputStream is = new FileInputStream(path)) {
            is.skip(off);
            return is.readNBytes(MAX_CHUNK_SIZE_BYTES);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return new byte[0];
        }
    }

    private int totalBytes(String path) {
        try(FileInputStream is = new FileInputStream(path)) {
            return is.available();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }
}
