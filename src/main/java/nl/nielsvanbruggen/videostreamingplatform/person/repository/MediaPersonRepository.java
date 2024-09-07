package nl.nielsvanbruggen.videostreamingplatform.person.repository;

import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.person.id.MediaPersonId;
import nl.nielsvanbruggen.videostreamingplatform.person.model.ContextRole;
import nl.nielsvanbruggen.videostreamingplatform.person.model.MediaPerson;
import nl.nielsvanbruggen.videostreamingplatform.person.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaPersonRepository extends JpaRepository<MediaPerson, MediaPersonId> {
    List<MediaPerson> findAllByMedia(Media media);
    List<MediaPerson> findAllByMediaAndContextRole(Media media, ContextRole contextRole);
    List<MediaPerson> deleteByMedia(Media media);
    List<MediaPerson> deleteByPersonAndContextRole(Person person, ContextRole contextRole);
}