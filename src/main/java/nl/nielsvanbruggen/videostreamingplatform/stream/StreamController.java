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
    public ResponseEntity<?> getVideo(@PathVariable @NotBlank long id, @RequestHeader HttpHeaders headers) {
        return streamService.getVideo(id, headers);
    }

    @GetMapping("/subtitle/{id}")
    public ResponseEntity<?> getSubtitle(@PathVariable @NotBlank long id, @RequestHeader HttpHeaders headers) {
        return streamService.getSubtitle(id, headers);
    }

    @GetMapping("/thumbnail/{id}")
    public ResponseEntity<?> getThumbnail(@PathVariable @NotBlank long id, @RequestHeader HttpHeaders headers) {
        return streamService.getThumbnail(id, headers);
    }
}
