package nl.nielsvanbruggen.videostreamingplatform.user.repository;

import nl.nielsvanbruggen.videostreamingplatform.user.model.UserActivity;
import nl.nielsvanbruggen.videostreamingplatform.user.model.UserActivityCountByHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    @Query(nativeQuery = true,
            value = "SELECT DATE_TRUNC('hour', u.created_at) AS timestamp, " +
            "COUNT(DISTINCT u.user_id) AS userCount " +
            "FROM user_activity u " +
            "GROUP BY timestamp " +
            "ORDER BY timestamp DESC")
    List<UserActivityCountByHour> allUserActivityCountByHour();
}
