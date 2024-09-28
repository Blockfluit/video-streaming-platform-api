package nl.nielsvanbruggen.videostreamingplatform.mediarequest;

import com.sun.jdi.InternalException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.service.MediaService;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
public class MediaRequestController {
    private final MediaRequestService mediaRequestService;
    private final MediaService mediaService;
    private final UserService userService;

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
        User user = userService.getUser(authentication.getName());

        mediaRequestService.patchMediaRequest(id, user, mediaRequestPatchRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMediaRequest(@PathVariable Long id) {
        mediaRequestService.deleteMediaRequest(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
