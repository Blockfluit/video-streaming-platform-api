package nl.nielsvanbruggen.videostreamingplatform.user.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;

@Data
public class UserPatchRequest {
    @NotBlank(message = "Username can't be empty.")
    @Size(min = 3, max = 20, message = "A username needs between 3 and 20 characters.")
    String username;
    @Email(message = "Not a valid email.")
    String email;
    @Size(min = 4, max = 64, message = "A password needs between 4 and 64 characters.")
    private String password;
    Role role;
}
