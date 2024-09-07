package nl.nielsvanbruggen.videostreamingplatform.person.id;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.person.model.ContextRole;
import nl.nielsvanbruggen.videostreamingplatform.person.model.Person;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class MediaPersonId implements Serializable {
    private Media media;
    private Person person;
    private ContextRole contextRole;
}
