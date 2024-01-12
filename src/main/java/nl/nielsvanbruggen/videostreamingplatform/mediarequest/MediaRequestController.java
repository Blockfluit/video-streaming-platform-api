package nl.nielsvanbruggen.videostreamingplatform.mediarequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/request")
@RequiredArgsConstructor
public class MediaRequestController {
    private final MediaRequestService mediaRequestService;

    @GetMapping
    public ResponseEntity<Page<MediaRequestDTO>> getMediaRequest(@RequestParam int pagenumber,
                                                                 @RequestParam int pagesize,
                                                                 @RequestParam String search) {
        return new ResponseEntity<>(mediaRequestService.getAllMediaRequests(pagenumber, pagesize, search), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postMediaRequest(@Valid @RequestBody MediaRequestPostRequest mediaRequestPostRequest, Authentication authentication) {
        mediaRequestService.postMediaRequest(mediaRequestPostRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchMediaRequest(@PathVariable Long id, @RequestBody MediaRequestPatchRequest mediaRequestPatchRequest, Authentication authentication) {
        mediaRequestService.patchMediaRequest(id, mediaRequestPatchRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMediaRequest(@PathVariable Long id) {
        mediaRequestService.deleteMediaRequest(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
