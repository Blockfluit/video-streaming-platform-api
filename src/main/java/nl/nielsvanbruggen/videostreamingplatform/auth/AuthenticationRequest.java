package nl.nielsvanbruggen.videostreamingplatform.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
