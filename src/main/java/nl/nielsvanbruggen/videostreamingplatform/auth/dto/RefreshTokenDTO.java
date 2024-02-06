package nl.nielsvanbruggen.videostreamingplatform.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class RefreshTokenDTO {
    private Long id;
    private String token;
    private Instant expiration;
    private Instant createdAt;
}
