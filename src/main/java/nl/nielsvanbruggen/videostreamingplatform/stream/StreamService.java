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

        return createFullResponseEntity(Path.of(absolutePath));
    }

    public ResponseEntity<?> getThumbnail(long id) {
        String path = streamCache.getThumbnailPath(id);
        String absolutePath = thumbnailRoot + "/" + path;

        return createFullResponseEntity(Path.of(absolutePath));
    }

    public ResponseEntity<?> getSnapshot(long id) {
        String path = streamCache.getSnapshotPath(id);
        String absolutePath = snapshotRoot + "/" + path;

        return createFullResponseEntity(Path.of(absolutePath));
    }

    private ResponseEntity<?> createStreamResponseEntity(Path path, HttpHeaders headers) {
        long tot = totalBytes(path);

        if (headers.getRange().size() == 0) {
            return createInitialResponse(path, tot);
        }

        String rangeHeader = headers.getRange().get(0).toString();
        long start = Long.parseLong(rangeHeader.split("-")[0]);

        if (rangeHeader.split("-").length == 1) {
            // MAX_CHUNK_SIZE_BYTES - 1, because its zero-based.
            long end = Math.min((start + MAX_CHUNK_SIZE_BYTES - 1), tot - 1);
            return createPartialResponse(path, start, end, tot);
        }

        long rangeEnd = Long.parseLong(rangeHeader.split("-")[1]);
        if (rangeEnd < MAX_CHUNK_SIZE_BYTES - 1) {
            return createPartialResponse(path, start, rangeEnd, tot);
        }

        long end = Math.min((start + MAX_CHUNK_SIZE_BYTES - 1), tot - 1);
        return createPartialResponse(path, start, end, tot);
    }

    private ResponseEntity<byte[]> createFullResponseEntity(Path path) {
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
            // Plus 1 because it's zero-based.
            return is.readNBytes((int) (end - start + 1));
        } catch (IOException e) {
            log.warn(String.format("Could not read %d to %d bytes: %s", start, end, e.getMessage()));
            return new byte[0];
        }
    }
    private long totalBytes(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            log.warn("Could not read total amount of bytes: " + e.getMessage());
            return 0;
        }
    }
}
