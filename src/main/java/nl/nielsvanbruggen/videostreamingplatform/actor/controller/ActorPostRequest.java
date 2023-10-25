package nl.nielsvanbruggen.videostreamingplatform.actor.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActorPostRequest {
    @NotBlank(message = "Firstname missing.")
    String firstname;
    String lastname;
}
