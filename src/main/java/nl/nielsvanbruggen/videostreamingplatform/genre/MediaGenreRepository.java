package nl.nielsvanbruggen.videostreamingplatform.genre;

import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaGenreRepository extends JpaRepository<MediaGenre, MediaGenreId> {
    List<MediaGenre> findAllByMedia(Media media);
}
