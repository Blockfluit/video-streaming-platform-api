package nl.nielsvanbruggen.videostreamingplatform.watched.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.service.UserService;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import nl.nielsvanbruggen.videostreamingplatform.video.service.VideoService;
import nl.nielsvanbruggen.videostreamingplatform.watched.service.WatchedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/watched")
@RequiredArgsConstructor
public class WatchedController {
    private final WatchedService watchedService;
    private final UserService userService;
    private final VideoService videoService;

    @GetMapping
    public ResponseEntity<WatchedGetResponse> getAllWatched(Authentication authentication) {
        User user = userService.getUser(authentication.getName());

        WatchedGetResponse response = WatchedGetResponse.builder()
                .allWatched(watchedService.getAllWatched(user))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postWatched(@Valid @RequestBody WatchedPostRequest watchedPostRequest, Authentication authentication) {
        User user = userService.getUser(authentication.getName());
        Video video = videoService.getVideo(watchedPostRequest.getId());

        watchedService.postWatched(user, video, watchedPostRequest.getTimestamp());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
