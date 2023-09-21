package nl.nielsvanbruggen.videostreamingplatform.actor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.actor.id.MediaActorId;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(MediaActorId.class)
public class MediaActor {
    @Id
    @ManyToOne
    private Media media;
    @Id
    @ManyToOne
    private Actor actor;
}
