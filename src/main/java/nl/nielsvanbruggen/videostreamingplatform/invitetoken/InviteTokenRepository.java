package nl.nielsvanbruggen.videostreamingplatform.invitetoken;

import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InviteTokenRepository extends JpaRepository<InviteToken, String> {
    List<InviteToken> findAllByCreatedBy(User user);
}
