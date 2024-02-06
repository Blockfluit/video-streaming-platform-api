package nl.nielsvanbruggen.videostreamingplatform.auth.repository;

import nl.nielsvanbruggen.videostreamingplatform.auth.model.RefreshToken;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteAllByUser(User user);
    void deleteAllByUserId(Long id);
    List<RefreshToken> findAllByUser(User user);
    Optional<RefreshToken> findByToken(String token);
}
