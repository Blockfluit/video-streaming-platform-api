package nl.nielsvanbruggen.videostreamingplatform.auth.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username can't be empty.")
    @Size(min = 3, max = 20, message = "A username needs between 3 and 20 characters.")
    private String username;
    @Email(message = "Not a valid email.")
    private String email;
    @NotBlank(message = "Password can't be empty.")
    @Size(min = 4, max = 64, message = "A password needs between 4 and 64 characters.")
    private String password;
    private Role role;
}
