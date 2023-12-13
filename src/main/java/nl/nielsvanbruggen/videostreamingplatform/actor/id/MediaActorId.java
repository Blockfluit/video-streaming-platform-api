package nl.nielsvanbruggen.videostreamingplatform.actor.id;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.actor.model.Actor;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class MediaActorId implements Serializable {
    private Media media;
    private Actor actor;
}
