package nl.nielsvanbruggen.videostreamingplatform.passwordreset;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetPostRequest {
    @NotBlank
    @Email
    private String email;
}
