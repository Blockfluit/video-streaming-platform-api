package nl.nielsvanbruggen.videostreamingplatform.media.repository;

import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.model.MediaRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRelationRepository extends JpaRepository<MediaRelation, Long> {
    List<MediaRelation> findAllByMediaFrom(Media media);
}
