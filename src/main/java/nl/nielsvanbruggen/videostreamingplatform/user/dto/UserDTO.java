package nl.nielsvanbruggen.videostreamingplatform.user.dto;

import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;

import java.time.Instant;

public record UserDTO(
        long id,
        String username,
        String email,
        Role role,
        Instant lastActiveAt,
        Instant lastLoginAt,
        Instant createdAt
) {
}
