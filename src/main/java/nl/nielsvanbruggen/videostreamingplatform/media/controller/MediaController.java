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

    @GetMapping("/{id}")
    public ResponseEntity<MediaGetResponse> getMedia(@PathVariable(required = false) Long id) {
        MediaGetResponse response = MediaGetResponse.builder()
                .media(mediaService.getMedia(id))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<AllMediaGetResponse> getAllMedia() {
        AllMediaGetResponse response = AllMediaGetResponse.builder()
                .allMedia(mediaService.getAllMedia())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/last-watched")
    public ResponseEntity<LastWatchedGetResponse> getLastWatchedMedia() {
        LastWatchedGetResponse response = LastWatchedGetResponse.builder()
                .lastWatched(mediaService.getLastWatchedMedia())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<Void> postRate(@PathVariable Long id, @Valid @RequestBody RatingPostRequest ratingPostRequest, Authentication authentication) {
        mediaService.postRating(id, ratingPostRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<?> postReview(@PathVariable Long id, @Valid @RequestBody ReviewPostRequest reviewPostRequest, Authentication authentication) {
        mediaService.postReview(id, reviewPostRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/review")
    public ResponseEntity<Void> patchReview(@PathVariable Long id, @Valid @RequestBody ReviewPatchRequest reviewPatchRequest, Authentication authentication) {
        mediaService.patchReview(id, reviewPatchRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/review")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, @Valid @RequestBody ReviewDeleteRequest reviewDeleteRequest, Authentication authentication) {
        mediaService.deleteReview(id, reviewDeleteRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> postMedia(@Valid @ModelAttribute MediaPostRequest mediaPostRequest) {
        mediaService.postMedia(mediaPostRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchMedia(@PathVariable Long id, @ModelAttribute MediaPatchRequest mediaPatchRequest) {
        mediaService.patchMedia(id, mediaPatchRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long id) {
        mediaService.deleteMedia(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
