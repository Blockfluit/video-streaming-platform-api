package nl.nielsvanbruggen.videostreamingplatform.stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.service.MediaService;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.service.UserService;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Subtitle;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import nl.nielsvanbruggen.videostreamingplatform.video.service.SubtitleService;
import nl.nielsvanbruggen.videostreamingplatform.video.service.VideoService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stream")
public class StreamController {
    private final VideoTokenService videoTokenService;
    private final FileService fileService;
    private final MediaService mediaService;
    private final SubtitleService subtitleService;
    private final VideoService videoService;
    private final UserService userService;

    @GetMapping("/video/{id}")
    public ResponseEntity<?> getVideo(@PathVariable Long id, @RequestParam UUID token, @RequestHeader HttpHeaders headers) {
        Video video = videoService.getVideo(id);
        Resource resource;

        try {
            resource = fileService.getVideo(video);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("video/mp4"))
                .body(resource);
    }

    @GetMapping("/subtitle/{id}")
    public ResponseEntity<Resource> getSubtitle(@PathVariable Long id) {
        Subtitle subtitle = subtitleService.getSubtitle(id);
        Resource resource;

        try {
            resource = fileService.getSubtitle(subtitle);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("text/vtt"))
                .body(resource);
    }

    @GetMapping("/thumbnail/{id}")
    public ResponseEntity<Resource> getThumbnail(@PathVariable Long id) {
        Media media = mediaService.getMedia(id);
        Resource resource;

        try {
            resource = fileService.getThumbnail(media);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @GetMapping("/snapshot/{id}")
    public ResponseEntity<Resource> getSnapshot(@PathVariable Long id) {
        Video video = videoService.getVideo(id);
        Resource resource;

        try {
            resource = fileService.getSnapshot(video);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @GetMapping("/video-token/{id}")
    public ResponseEntity<VideoTokenGetResponse> getVideoToken(@PathVariable Long id, Authentication authentication) {
        Video video = videoService.getVideo(id);
        User user = userService.getUser(authentication.getName());
        VideoToken videoToken = videoTokenService.getVideoToken(user, video);

        // Reset the expiration and persist it.
        videoToken.resetExpiration();
        videoTokenService.saveToken(videoToken);

        VideoTokenGetResponse response = VideoTokenGetResponse.builder()
                .token(videoToken.getToken().toString())
                .build();

        return ResponseEntity.ok(response);
    }
}
