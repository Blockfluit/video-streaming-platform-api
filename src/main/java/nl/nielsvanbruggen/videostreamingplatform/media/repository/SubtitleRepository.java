package nl.nielsvanbruggen.videostreamingplatform.media.repository;

import nl.nielsvanbruggen.videostreamingplatform.media.model.Subtitle;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Video;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubtitleRepository extends CrudRepository<Subtitle, Long> {
    List<Subtitle> findAllByVideo(Video video);
}
