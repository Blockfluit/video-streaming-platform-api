package nl.nielsvanbruggen.videostreamingplatform.invitetoken;

import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;

import java.time.Instant;

public record InviteTokenDTO(String token,
                             Instant expiration,
                             Instant createdAt,
                             Role role,
                             String createdBy) {
}
