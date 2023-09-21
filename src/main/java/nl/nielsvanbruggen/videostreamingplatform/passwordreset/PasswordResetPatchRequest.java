package nl.nielsvanbruggen.videostreamingplatform.passwordreset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetPatchRequest {
    @NotBlank(message = "Password can't be empty.")
    @Size(min = 4, max = 64, message = "A password needs between 4 and 64 characters.")
    private String password;
}
