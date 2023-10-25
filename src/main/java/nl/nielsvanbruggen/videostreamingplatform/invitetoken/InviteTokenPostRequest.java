package nl.nielsvanbruggen.videostreamingplatform.invitetoken;

import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;

import java.time.Instant;

@Data
public class InviteTokenPostRequest {
    private Instant expiration;
    private Role role;
    private boolean master;
}
