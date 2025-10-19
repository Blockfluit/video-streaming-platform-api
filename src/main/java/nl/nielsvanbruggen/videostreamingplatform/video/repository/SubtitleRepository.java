package nl.nielsvanbruggen.videostreamingplatform.video.repository;

import nl.nielsvanbruggen.videostreamingplatform.video.model.Subtitle;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubtitleRepository extends JpaRepository<Subtitle, Long> {
    List<Subtitle> findAllByVideo(Video video);
    void deleteAllByVideo(Video video);
    List<Subtitle> deleteByVideoIn(List<Video> videos);
}
