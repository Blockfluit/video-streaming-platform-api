package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.service.MediaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/media")
public class MediaController {
    private final MediaService mediaService;

    @GetMapping({"/", "/{id}"})
    public ResponseEntity<?> getMedia(@PathVariable(required = false) Long id) {
        return new ResponseEntity<>(mediaService.getMedia(id), HttpStatus.OK);
    }

    // TODO: add patch method for updating the media

    @PostMapping
    public ResponseEntity<?> postMedia(@Valid @ModelAttribute MediaPostRequest mediaPostRequest) {
        return mediaService.postMedia(mediaPostRequest);
    }
}
