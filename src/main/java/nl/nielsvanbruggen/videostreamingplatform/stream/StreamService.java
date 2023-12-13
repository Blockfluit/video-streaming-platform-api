package nl.nielsvanbruggen.videostreamingplatform.stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class StreamService {
    private final StreamCache streamCache;
    private final static int MAX_CHUNK_SIZE_BYTES = 1024 * 1024;
    @Value("${env.thumbnail.root}")
    private String thumbnailRoot;
    @Value("${env.snapshot.root}")
    private String snapshotRoot;
    @Value("${env.videos.root}")
    private String videosRoot;

    public ResponseEntity<?> getVideo(long id, HttpHeaders headers) {
        String path = streamCache.getVideoPath(id);
        String absolutePath = videosRoot + "/" + path;

        return createStreamResponseEntity(Path.of(absolutePath), headers);
    }

    public ResponseEntity<?> getSubtitle(long id) {
        String path = streamCache.getSubtitlePath(id);
        String absolutePath = videosRoot + "/" + path;

        return createCompleteResponseEntity(Path.of(absolutePath));
    }

    public ResponseEntity<?> getThumbnail(long id) {
        String path = streamCache.getThumbnailPath(id);
        String absolutePath = thumbnailRoot + "/" + path;

        return createCompleteResponseEntity(Path.of(absolutePath));
    }

    public ResponseEntity<?> getSnapshot(long id) {
        String path = streamCache.getSnapshotPath(id);
        String absolutePath = snapshotRoot + "/" + path;

        return createCompleteResponseEntity(Path.of(absolutePath));
    }

    private ResponseEntity<?> createStreamResponseEntity(Path path, HttpHeaders headers) {
        long tot = totalBytes(path);

        if(headers.getRange().size() == 0) {
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

    private ResponseEntity<Void> createInitialResponse(Path path, long tot) {
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
