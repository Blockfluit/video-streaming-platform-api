package nl.nielsvanbruggen.videostreamingplatform.stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.nielsvanbruggen.videostreamingplatform.config.EnvironmentProperties;
import nl.nielsvanbruggen.videostreamingplatform.global.exception.ResourceNotFoundException;
import nl.nielsvanbruggen.videostreamingplatform.global.util.MimeTypeUtil;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.MediaRepository;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Subtitle;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import nl.nielsvanbruggen.videostreamingplatform.video.repository.SubtitleRepository;
import nl.nielsvanbruggen.videostreamingplatform.video.repository.VideoRepository;
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
    private final static int MAX_CHUNK_SIZE_BYTES = 1024 * 1024;
    private final EnvironmentProperties env;

    public ResponseEntity<?> getVideo(Video video, HttpHeaders headers) {
        Path absolutePath = Path.of(env.getVideos().get("root") + video.getPath());

        long tot = totalBytes(absolutePath);
        return createStreamResponseEntity(absolutePath, headers, tot);
    }

    public ResponseEntity<byte[]> getSubtitle(Subtitle subtitle) {
        Path absolutePath = Path.of(env.getVideos().get("root") + subtitle.getPath());

        long tot = totalBytes(absolutePath);
        byte[] bytes = readBytes(absolutePath, 0, tot);
        return createFullResponseEntity(absolutePath, bytes, tot);
    }

    public ResponseEntity<byte[]> getThumbnail(Media media) {
        String path = media.getThumbnail();
        Path absolutePath = Path.of(env.getThumbnail().get("root") + path);

        long tot = totalBytes(absolutePath);
        byte[] bytes = readBytes(absolutePath, 0, tot);
        return createFullResponseEntity(absolutePath, bytes, tot);
    }

    public ResponseEntity<byte[]> getSnapshot(Video video) {
        Path absolutePath = Path.of(env.getSnapshot().get("root") + video.getSnapshot());

        long tot = totalBytes(absolutePath);
        byte[] bytes = readBytes(absolutePath, 0, tot);
        return createFullResponseEntity(absolutePath, bytes, tot);
    }

    private ResponseEntity<?> createStreamResponseEntity(Path path, HttpHeaders headers, long tot) {
        // Handles case were no range header is provided.
        if (headers.getRange().size() == 0) {
            return createInitialResponse(path, tot);
        }

        String[] rangeHeader = headers.getRange().get(0).toString().split("-");
        long start = Long.parseLong(rangeHeader[0]);
        long end = rangeHeader.length == 1 ?
                // Handles case were only first number of range header is provided.
                // This is the standard for most browsers.
                Math.min((start + MAX_CHUNK_SIZE_BYTES), tot - 1) :
                // Handles case were both numbers of range header is provided.
                // As far as I know, only Apple does this.
                Long.parseLong(rangeHeader[1]);

        byte[] bytes = readBytes(path, start, end);
        return createPartialResponse(path, bytes, start, end, tot);
    }

    private ResponseEntity<byte[]> createFullResponseEntity(Path path, byte[] bytes, long tot) {
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
        responseHeaders.add("Cache-Control", "no-store, no-cache");

        return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
    }

    private ResponseEntity<byte[]> createPartialResponse(Path path, byte[] bytes, long start, long end, long tot) {
        MultiValueMap<String, String> responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Range", String.format("bytes %1$d-%2$d/%3$d", start, end, tot));
        responseHeaders.add("Accept-Ranges", "bytes");
        responseHeaders.add("Content-Type", MimeTypeUtil.getMimeType(path));
        responseHeaders.add("Content-Length", String.valueOf(bytes.length));
        responseHeaders.add("Cache-Control", "no-store, no-cache");

        return new ResponseEntity<>(bytes, responseHeaders, HttpStatus.PARTIAL_CONTENT);
    }

    private byte[] readBytes(Path path, long start, long end) {
       try(InputStream is = Files.newInputStream(path)) {
            is.skip(start);
            // + 1 because start and end are inclusive.
            // For example bytes 0-1/100 should return byte[0] and byte[1]
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
            return -1;
        }
    }
}
