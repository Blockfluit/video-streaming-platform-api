package nl.nielsvanbruggen.videostreamingplatform.user.repository;

import nl.nielsvanbruggen.videostreamingplatform.user.model.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
}
