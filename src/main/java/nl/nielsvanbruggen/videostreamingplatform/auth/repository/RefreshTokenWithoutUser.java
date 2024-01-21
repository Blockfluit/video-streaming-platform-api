package nl.nielsvanbruggen.videostreamingplatform.auth.repository;

import java.time.Instant;

public interface RefreshTokenWithoutUser {
    Long getId();
    String getToken();
    Instant getExpiration();
    Instant getCreatedAt();
}
