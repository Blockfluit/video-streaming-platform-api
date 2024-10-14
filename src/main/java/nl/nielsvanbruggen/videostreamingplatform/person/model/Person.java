package nl.nielsvanbruggen.videostreamingplatform.person.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {
    @Id
    @GeneratedValue
    private long id;
    @Column(unique = true)
    private String imdbId;
    private String firstname;
    private String lastname;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDate dateOfBirth;
    private LocalDate dateOfDeath;
}
