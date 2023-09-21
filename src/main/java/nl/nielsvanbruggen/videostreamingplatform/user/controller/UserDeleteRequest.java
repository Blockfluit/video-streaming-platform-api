package nl.nielsvanbruggen.videostreamingplatform.user.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDeleteRequest {
    @NotBlank
    String username;
}
