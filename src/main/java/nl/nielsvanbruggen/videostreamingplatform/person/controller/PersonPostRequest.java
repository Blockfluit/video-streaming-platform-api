package nl.nielsvanbruggen.videostreamingplatform.person.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonPostRequest {
    private String imdbId;
    @NotBlank(message = "Firstname missing.")
    private String firstname;
    private String lastname;
    private String description;
    private LocalDate dateOfBirth;
    private LocalDate dateOfDeath;
}
