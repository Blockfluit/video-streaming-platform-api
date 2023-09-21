package nl.nielsvanbruggen.videostreamingplatform.invitetoken;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InviteTokenDeleteRequest {
    @NotBlank
    private String token;
}
