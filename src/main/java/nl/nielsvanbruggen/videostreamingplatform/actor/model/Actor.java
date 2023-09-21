package nl.nielsvanbruggen.videostreamingplatform.actor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Actor {
    @Id
    @GeneratedValue
    private long id;
    private String firstname;
    private String lastname;
}
