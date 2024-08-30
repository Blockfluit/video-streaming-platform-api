package nl.nielsvanbruggen.videostreamingplatform.stream;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoTokenService {
    private final static int VIDEO_EXPIRATION_IN_MINUTES = 60;
    private final VideoTokenRepository videoTokenRepository;

    public VideoToken getVideoToken(String token) {
        return videoTokenRepository.findByToken(token)
                .orElseThrow(() -> new VideoTokenException("Video token does not exist."));
    }

    public boolean isTokenValid(VideoToken videoToken) {
        return Instant.now()
                .isBefore(videoToken.getExpiration().plus(VIDEO_EXPIRATION_IN_MINUTES, ChronoUnit.MINUTES));
    }

    public void updateVideoToken(VideoToken videoToken) {
        videoToken.setExpiration(getExpiration());
        videoTokenRepository.save(videoToken);
    }

    @Transactional
    public VideoToken createVideoToken(User user, Video video) {
        videoTokenRepository.deleteAllByVideo(video);

        VideoToken token = VideoToken.builder()
                .token(UUID.randomUUID().toString())
                .video(video)
                .createdAt(Instant.now())
                .expiration(getExpiration())
                .user(user)
                .build();

        videoTokenRepository.save(token);

        return token;
    }

    private Instant getExpiration() {
        return Instant.now().plus(VIDEO_EXPIRATION_IN_MINUTES, ChronoUnit.MINUTES);
    }
}
