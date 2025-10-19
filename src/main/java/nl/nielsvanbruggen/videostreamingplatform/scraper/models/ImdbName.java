package nl.nielsvanbruggen.videostreamingplatform.scraper.models;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ImdbName {
    private String imdbId;
    private String firstname;
    private String lastname;
    private String description;
    private List<String> roles;
    private LocalDate dateOfBirth;
    private LocalDate dateOfDeath;
}
