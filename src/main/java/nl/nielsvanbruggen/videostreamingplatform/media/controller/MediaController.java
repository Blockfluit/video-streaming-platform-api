package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTO;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTOMapper;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.service.IndexService;
import nl.nielsvanbruggen.videostreamingplatform.media.service.MediaService;
import nl.nielsvanbruggen.videostreamingplatform.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequiredArgsConstructor
@RequestMapping("/media")
public class MediaController {

    private static final AtomicBoolean INDEXING = new AtomicBoolean(false);

    private final MediaService mediaService;
    private final UserService userService;
    private final MediaDTOMapper mediaDTOMapper;
    private final IndexService indexService;

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
                                                      @RequestParam(required = false, defaultValue = "") String search,
                                                      Authentication authentication) {
        var page = mediaService.getMedia(pagenumber, pagesize, type, genres, search, userService.isAdmin(authentication));

        return ResponseEntity.ok(page);
    }

    @GetMapping("/auto-completion")
    public ResponseEntity<Page<Media>> getAutoCompletion(@RequestParam int pagenumber,
                                                         @RequestParam int pagesize,
                                                         @RequestParam(required = false, defaultValue = "") String type,
                                                         @RequestParam(required = false, defaultValue = "") List<String> genres,
                                                         @RequestParam String search,
                                                         Authentication authentication) {
        var page = mediaService.getAutocompletion(pagenumber, pagesize, type, genres, search, userService.isAdmin(authentication));

        return ResponseEntity.ok(page);
    }

    @GetMapping("/recent-uploaded")
    public ResponseEntity<Page<MediaDTO>> getRecentUploaded(@RequestParam int pagenumber,
                                                            @RequestParam int pagesize,
                                                            @RequestParam(required = false, defaultValue = "") String type,
                                                            Authentication authentication) {
        var page = mediaService.getRecentUploaded(pagenumber, pagesize, type, userService.isAdmin(authentication));

        return ResponseEntity.ok(page);
    }

    @GetMapping("/best-rated")
    public ResponseEntity<Page<MediaDTO>> getBestRated(@RequestParam int pagenumber,
                                                       @RequestParam int pagesize,
                                                       @RequestParam(required = false, defaultValue = "") String type,
                                                       Authentication authentication) {
        var page = mediaService.getBestRated(pagenumber, pagesize, type, userService.isAdmin(authentication));

        return ResponseEntity.ok(page);
    }

    @GetMapping("/most-watched")
    public ResponseEntity<Page<MediaDTO>> getMostWatched(@RequestParam int pagenumber,
                                                         @RequestParam int pagesize,
                                                         @RequestParam(required = false, defaultValue = "") String type,
                                                         Authentication authentication) {
        var page = mediaService.getMostWatched(pagenumber, pagesize, type, userService.isAdmin(authentication));

        return ResponseEntity.ok(page);
    }

    @GetMapping("/last-watched")
    public ResponseEntity<Page<MediaDTO>> getLastWatched(@RequestParam int pagenumber,
                                                         @RequestParam int pagesize,
                                                         @RequestParam(required = false, defaultValue = "") String type,
                                                         Authentication authentication) {
        var page = mediaService.getLastWatched(pagenumber, pagesize, type, userService.isAdmin(authentication));

        return ResponseEntity.ok(page);
    }

    @GetMapping("/recent-watched")
    public ResponseEntity<Page<MediaDTO>> getRecentWatched(Authentication authentication,
                                                           @RequestParam int pagenumber,
                                                           @RequestParam int pagesize,
                                                           @RequestParam(required = false, defaultValue = "") String type) {
        var page = mediaService.getRecentWatched(userService.getUser(authentication), pagenumber, pagesize, type);

        return ResponseEntity.ok(page);
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<Void> postRate(@PathVariable Long id,
                                         @Valid @RequestBody RatingPostRequest ratingPostRequest,
                                         Authentication authentication) {
        mediaService.postRating(id, ratingPostRequest, authentication);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<?> postReview(@PathVariable Long id,
                                        @Valid @RequestBody ReviewPostRequest reviewPostRequest,
                                        Authentication authentication) {
        mediaService.postReview(id, reviewPostRequest, authentication);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/review")
    public ResponseEntity<Void> patchReview(@PathVariable Long id,
                                            @Valid @RequestBody ReviewPatchRequest reviewPatchRequest,
                                            Authentication authentication) {
        mediaService.patchReview(id, reviewPatchRequest, authentication);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/review")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id,
                                             @Valid @RequestBody ReviewDeleteRequest reviewDeleteRequest,
                                             Authentication authentication) {
        mediaService.deleteReview(id, reviewDeleteRequest, authentication);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> postMedia(@Valid @RequestBody MediaPostRequest mediaPostRequest,
                                          Authentication authentication) {
        mediaService.postMedia(mediaPostRequest, authentication);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchMedia(@PathVariable Long id,
                                           @Valid @RequestBody MediaPatchRequest mediaPatchRequest) {
        mediaService.patchMedia(id, mediaPatchRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long id) {
        mediaService.deleteMedia(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/index-all")
    public ResponseEntity<Void> indexAll() {
        if(INDEXING.compareAndSet(false, true)) {
            new Thread(() -> {
                indexService.indexAll();
                INDEXING.set(false);
            }).start();

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.accepted().build();
    }
}
