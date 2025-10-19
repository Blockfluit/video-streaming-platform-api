package nl.nielsvanbruggen.videostreamingplatform.person.id;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.person.model.GlobalRole;
import nl.nielsvanbruggen.videostreamingplatform.person.model.Person;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class GlobalRolePersonId implements Serializable {
    private GlobalRole globalRole;
    private Person person;
}
