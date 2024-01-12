package nl.nielsvanbruggen.videostreamingplatform.user.model;

import java.time.Instant;

public interface UserActivityCountByHour {
    Instant getTimestamp();
    Long getUserCount();
}
