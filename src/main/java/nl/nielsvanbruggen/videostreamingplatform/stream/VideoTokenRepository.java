package nl.nielsvanbruggen.videostreamingplatform.stream;

import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VideoTokenRepository extends JpaRepository<VideoToken, UUID> {
    Optional<VideoToken> findByUserAndVideo(User user, Video video);
    Optional<VideoToken> findByToken(UUID token);
    void deleteAllByUser(User user);
    void deleteByVideoIn(List<Video> videos);
    void deleteAllByVideo(Video video);
}
