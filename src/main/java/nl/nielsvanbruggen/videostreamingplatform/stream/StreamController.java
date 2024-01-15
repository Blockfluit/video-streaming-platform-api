package nl.nielsvanbruggen.videostreamingplatform.stream;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.service.UserService;
import nl.nielsvanbruggen.videostreamingplatform.video.exception.VideoException;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import nl.nielsvanbruggen.videostreamingplatform.video.service.VideoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stream")
public class StreamController {
    private final StreamService streamService;
    private final VideoTokenService videoTokenService;
    private final VideoService videoService;
    private final UserService userService;

    @GetMapping("/video")
    public ResponseEntity<?> getVideo(@RequestParam String token, @RequestHeader HttpHeaders headers) {
        VideoToken videoToken = videoTokenService.getVideoToken(token);
        if(!videoTokenService.isTokenValid(videoToken)) {
            throw new VideoTokenException("Video token is not valid.");
        }

        videoTokenService.updateVideoToken(videoToken);
        return streamService.getVideo(videoToken.getVideo(), headers);
    }

    @GetMapping("/subtitle/{id}")
    public ResponseEntity<byte[]> getSubtitle(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        return streamService.getSubtitle(id);
    }

    @GetMapping("/thumbnail/{id}")
    public ResponseEntity<byte[]> getThumbnail(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        return streamService.getThumbnail(id);
    }

    @GetMapping("/snapshot/{id}")
    public ResponseEntity<byte[]> getSnapshot(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        return streamService.getSnapshot(id);
    }

    @GetMapping("/video-token/{id}")
    public ResponseEntity<VideoTokenGetResponse> getVideoToken(@PathVariable Long id, Authentication authentication) {
        Video video = videoService.getVideo(id);
        User user = userService.getUser(authentication.getName());
        VideoToken token = videoTokenService.createVideoToken(user, video);

        VideoTokenGetResponse response = VideoTokenGetResponse.builder()
                .token(token.getToken())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
