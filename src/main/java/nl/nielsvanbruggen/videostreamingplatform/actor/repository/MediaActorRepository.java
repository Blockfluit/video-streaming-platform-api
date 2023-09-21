package nl.nielsvanbruggen.videostreamingplatform.actor.repository;

import nl.nielsvanbruggen.videostreamingplatform.actor.model.Actor;
import nl.nielsvanbruggen.videostreamingplatform.actor.model.MediaActor;
import nl.nielsvanbruggen.videostreamingplatform.actor.id.MediaActorId;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaActorRepository extends JpaRepository<MediaActor, MediaActorId> {
    List<MediaActor> findAllByMedia(Media media);
}
