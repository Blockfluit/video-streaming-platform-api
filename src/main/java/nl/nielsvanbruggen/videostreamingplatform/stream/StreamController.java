package nl.nielsvanbruggen.videostreamingplatform.stream;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stream")
public class StreamController {
    private final StreamService streamService;

    @GetMapping("/video/{id}")
    public ResponseEntity<?> getVideo(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        return streamService.getVideo(id, headers);
    }

    @GetMapping("/subtitle/{id}")
    public ResponseEntity<?> getSubtitle(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        return streamService.getSubtitle(id);
    }

    @GetMapping("/thumbnail/{id}")
    public ResponseEntity<?> getThumbnail(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        return streamService.getThumbnail(id);
    }

    @GetMapping("/snapshot/{id}")
    public ResponseEntity<?> getSnapshot(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        return streamService.getSnapshot(id);
    }
}
