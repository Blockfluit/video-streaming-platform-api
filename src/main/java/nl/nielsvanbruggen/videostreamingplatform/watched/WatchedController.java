package nl.nielsvanbruggen.videostreamingplatform.watched;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/watched")
@RequiredArgsConstructor
public class WatchedController {
    private final WatchedService watchedService;

    @GetMapping
    public ResponseEntity<?> getWatched(Authentication authentication) {
        return new ResponseEntity<>(watchedService.getWatched(authentication), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postWatched(@Valid @RequestBody WatchedPostRequest watchedPostRequest, Authentication authentication) {
        watchedService.postWatched(watchedPostRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
