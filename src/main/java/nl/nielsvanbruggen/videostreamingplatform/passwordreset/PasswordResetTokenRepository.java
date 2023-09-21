package nl.nielsvanbruggen.videostreamingplatform.passwordreset;

import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
    void deleteAllByUser(User user);
}
