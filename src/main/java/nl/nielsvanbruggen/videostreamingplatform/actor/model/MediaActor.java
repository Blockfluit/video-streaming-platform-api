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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    private Media media;
    @Id
    @ManyToOne
    @JoinColumn(name = "actor_id")
    private Actor actor;
}
