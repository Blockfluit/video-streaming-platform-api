package nl.nielsvanbruggen.videostreamingplatform.stream;

import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StreamServiceTest {
    @Autowired
    private StreamService streamService;

    @Test
    public void testByteRangeZeroToOne() {
        Video video = Video.builder()
                .path("test.mp4")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setRange(List.of(HttpRange.createByteRange(0, 1)));

        String expected = "bytes 0-1/";
        String actual = streamService.getVideo(video, headers).getHeaders().get("Content-Range").get(0);

        assertThat(actual)
                .contains(expected);
    }
}
