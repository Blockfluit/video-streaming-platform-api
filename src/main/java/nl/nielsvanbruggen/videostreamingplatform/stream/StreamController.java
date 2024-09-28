package nl.nielsvanbruggen.videostreamingplatform.stream;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.service.MediaService;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.service.UserService;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Subtitle;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import nl.nielsvanbruggen.videostreamingplatform.video.service.SubtitleService;
import nl.nielsvanbruggen.videostreamingplatform.video.service.VideoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/stream")
public class StreamController {
    private final VideoTokenService videoTokenService;
    private final StreamService streamService;
    private final MediaService mediaService;
    private final SubtitleService subtitleService;
    private final VideoService videoService;
    private final UserService userService;

    @GetMapping("/video/{id}")
    public ResponseEntity<?> getVideo(@PathVariable Long id, @RequestParam String token, @RequestHeader HttpHeaders headers) {
        VideoToken videoToken = videoTokenService.getVideoToken(token);
        Video video = videoService.getVideo(id);

        if(video.equals(videoToken.getVideo()) && !videoTokenService.isTokenValid(videoToken)) {
            throw new VideoTokenException("Video token is not valid.");
        }

        videoTokenService.updateVideoToken(videoToken);
        return streamService.getVideo(video, headers);
    }

    @GetMapping("/subtitle/{id}")
    public ResponseEntity<byte[]> getSubtitle(@PathVariable Long id) {
        Subtitle subtitle = subtitleService.getSubtitle(id);
        return streamService.getSubtitle(subtitle);
    }

    @GetMapping("/thumbnail/{id}")
    public ResponseEntity<byte[]> getThumbnail(@PathVariable Long id) {
        Media media = mediaService.getMedia(id);
        return streamService.getThumbnail(media);
    }

    @GetMapping("/snapshot/{id}")
    public ResponseEntity<byte[]> getSnapshot(@PathVariable Long id) {
        Video video = videoService.getVideo(id);
        return streamService.getSnapshot(video);
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
