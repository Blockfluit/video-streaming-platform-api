package nl.nielsvanbruggen.videostreamingplatform.invitetoken;

import lombok.Builder;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;

import java.time.Instant;

@Data
@Builder
public class InviteTokenDTO {
        private String token;
        private Instant expiration;
        private boolean used;
        private boolean master;
        private Instant createdAt;
        private Role role;
        private String createdBy;
}
