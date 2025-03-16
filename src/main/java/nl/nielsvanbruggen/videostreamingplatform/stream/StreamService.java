package nl.nielsvanbruggen.videostreamingplatform.stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.nielsvanbruggen.videostreamingplatform.config.PathProperties;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class StreamService {
    private final static int MAX_CHUNK_SIZE_BYTES = 1024 * 1024;

    private final PathProperties pathProperties;

    public ResponseEntity<?> createVideoResponse(Video video, HttpHeaders headers) {
        Path path = Path.of(pathProperties.getVideos().getRoot(), video.getPath());

        try (FileChannel fileChannel = FileChannel.open(path)) {
            long totalSize = fileChannel.size();

            if (headers.getRange().isEmpty()) {
                return createInitialResponse(totalSize);
            }

            HttpRange range = HttpRange.parseRanges(headers.getFirst(HttpHeaders.RANGE)).getFirst();
            long start = range.getRangeStart(totalSize);
            long end = range.getRangeEnd(totalSize);

            // Fallback logic if no end is provided.
            if (end == -1 || end >= totalSize - 1) {
                end = Math.min(start + MAX_CHUNK_SIZE_BYTES - 1, totalSize - 1);
            }

            int length = (int) (end - start + 1);
            end = Math.min(end, totalSize - 1);

            ByteBuffer buffer = ByteBuffer.allocate(length);
            fileChannel.position(start);
            fileChannel.read(buffer);
            buffer.flip();

            return createPartialResponse(buffer.array(), start, end, totalSize);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private ResponseEntity<Void> createInitialResponse(long tot) {
        return ResponseEntity.ok()
                .contentLength(tot)
                .header("Accept-Ranges", "bytes")
                .header("Content-Type", "video/mp4")
                .build();
    }

    private ResponseEntity<byte[]> createPartialResponse(byte[] bytes, long start, long end, long tot) {
        return ResponseEntity
                .status(HttpStatus.PARTIAL_CONTENT)
                .contentLength(bytes.length)
                .header("Content-Range", String.format("bytes %d-%d/%d", start, end, tot))
                .header("Accept-Ranges", "bytes")
                .header("Content-Type", "video/mp4")
                .body(bytes);
    }
}
