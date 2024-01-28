package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.genre.Genre;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTO;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTOMapper;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Type;
import nl.nielsvanbruggen.videostreamingplatform.media.service.MediaService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/media")
public class MediaController {
    private final MediaService mediaService;
    private final MediaDTOMapper mediaDTOMapper;

    @GetMapping("/{id}")
    public ResponseEntity<MediaGetResponse> getMedia(@PathVariable int id) {
        MediaGetResponse response = MediaGetResponse.builder()
                .media(mediaDTOMapper.apply(mediaService.getMedia(id)))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Page<MediaDTO>> getAllMedia(@RequestParam int pagenumber,
                                                      @RequestParam int pagesize,
                                                      @RequestParam(required = false, defaultValue = "") String type,
                                                      @RequestParam(required = false, defaultValue = "") List<String> genres,
                                                      @RequestParam(required = false, defaultValue = "") String search) {
        return new ResponseEntity<>(mediaService.getAllMedia(pagenumber, pagesize, type, genres, search), HttpStatus.OK);
    }

    @GetMapping("/auto-completion")
    public ResponseEntity<Page<Media>> getAutoCompletion(@RequestParam int pagenumber,
                                                         @RequestParam int pagesize,
                                                         @RequestParam(required = false, defaultValue = "") String type,
                                                         @RequestParam(required = false, defaultValue = "") List<String> genres,
                                                         @RequestParam String search) {
        return new ResponseEntity<>(mediaService.getAutocompletion(pagenumber, pagesize, type, genres, search), HttpStatus.OK);
    }

    @GetMapping("/recent-uploaded")
    public ResponseEntity<Page<MediaDTO>> getRecentUploaded(@RequestParam int pagenumber,
                                                            @RequestParam int pagesize,
                                                            @RequestParam(required = false, defaultValue = "") String type) {
        return new ResponseEntity<>(mediaService.getRecentUploaded(pagenumber, pagesize, type), HttpStatus.OK);
    }

    @GetMapping("/best-rated")
    public ResponseEntity<Page<MediaDTO>> getBestRated(@RequestParam int pagenumber,
                                                       @RequestParam int pagesize,
                                                       @RequestParam(required = false, defaultValue = "") String type) {
        return new ResponseEntity<>(mediaService.getBestRated(pagenumber, pagesize, type), HttpStatus.OK);
    }

    @GetMapping("/most-watched")
    public ResponseEntity<Page<MediaDTO>> getMostWatched(@RequestParam int pagenumber,
                                                         @RequestParam int pagesize,
                                                         @RequestParam(required = false, defaultValue = "") String type) {
        return new ResponseEntity<>(mediaService.getMostWatched(pagenumber, pagesize, type), HttpStatus.OK);
    }

    @GetMapping("/last-watched")
    public ResponseEntity<Page<MediaDTO>> getLastWatched(@RequestParam int pagenumber,
                                                         @RequestParam int pagesize,
                                                         @RequestParam(required = false, defaultValue = "") String type) {
        return new ResponseEntity<>(mediaService.getLastWatched(pagenumber, pagesize, type), HttpStatus.OK);
    }

    @GetMapping("/recent-watched")
    public ResponseEntity<Page<MediaDTO>> getRecentWatched(Authentication authentication,
                                                           @RequestParam int pagenumber,
                                                           @RequestParam int pagesize,
                                                           @RequestParam(required = false, defaultValue = "") String type) {
        return new ResponseEntity<>(mediaService.getRecentWatched(authentication, pagenumber, pagesize, type), HttpStatus.OK);
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
