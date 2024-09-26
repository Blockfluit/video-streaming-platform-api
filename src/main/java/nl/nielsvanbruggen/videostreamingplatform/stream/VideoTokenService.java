package nl.nielsvanbruggen.videostreamingplatform.stream;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class VideoTokenService {
    private final VideoTokenRepository videoTokenRepository;

    public VideoToken getVideoToken(UUID token) {
        return videoTokenRepository.findByToken(token)
                .orElseThrow(() -> new VideoTokenException(format("Token: (%s) does not exist!", token)));
    }

    @Transactional
    public VideoToken getVideoToken(User user, Video video) {
        return videoTokenRepository.findByUserAndVideo(user, video)
                .orElseGet(() -> videoTokenRepository.save(
                        VideoToken.builder()
                                .video(video)
                                .createdAt(Instant.now())
                                .user(user)
                                .build())
                );
    }
}
