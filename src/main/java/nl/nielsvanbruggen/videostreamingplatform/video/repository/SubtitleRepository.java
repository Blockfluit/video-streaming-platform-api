package nl.nielsvanbruggen.videostreamingplatform.video.repository;

import nl.nielsvanbruggen.videostreamingplatform.video.model.Subtitle;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubtitleRepository extends CrudRepository<Subtitle, Long> {
    List<Subtitle> findAllByVideo(Video video);
    void deleteAllByVideo(Video video);
    List<Subtitle> deleteByVideoIn(List<Video> videos);
}
