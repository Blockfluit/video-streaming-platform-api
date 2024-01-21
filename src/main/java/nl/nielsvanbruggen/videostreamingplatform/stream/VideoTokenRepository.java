package nl.nielsvanbruggen.videostreamingplatform.stream;

import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoTokenRepository extends JpaRepository<VideoToken, Long> {
    void deleteAllByUser(User user);
    Optional<VideoToken> findByToken(String token);
}
