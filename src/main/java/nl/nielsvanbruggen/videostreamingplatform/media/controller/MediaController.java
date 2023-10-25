package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.service.MediaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/media")
public class MediaController {
    private final MediaService mediaService;

    @GetMapping({"/", "/{id}"})
    public ResponseEntity<?> getMedia(@PathVariable(required = false) Long id, Authentication authentication) {
        return new ResponseEntity<>(mediaService.getMedia(id, authentication), HttpStatus.OK);
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<?> postRate(@PathVariable Long id, @Valid @RequestBody RatingPostRequest ratingPostRequest, Authentication authentication) {
        mediaService.postRating(id, ratingPostRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<?> postReview(@PathVariable Long id, @Valid @RequestBody ReviewPostRequest reviewPostRequest, Authentication authentication) {
        mediaService.postReview(id, reviewPostRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/review")
    public ResponseEntity<?> patchReview(@PathVariable Long id, @Valid @RequestBody ReviewPatchRequest reviewPatchRequest, Authentication authentication) {
        mediaService.patchReview(id, reviewPatchRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/review")
    public ResponseEntity<?> deleteReview(@PathVariable Long id, @Valid @RequestBody ReviewDeleteRequest reviewDeleteRequest, Authentication authentication) {
        mediaService.deleteReview(id, reviewDeleteRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postMedia(@Valid @ModelAttribute MediaPostRequest mediaPostRequest) {
        mediaService.postMedia(mediaPostRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchMedia(@PathVariable Long id, @ModelAttribute MediaPatchRequest mediaPatchRequest) {
        mediaService.patchMedia(id, mediaPatchRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedia(@PathVariable Long id) {
        mediaService.deleteMedia(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
