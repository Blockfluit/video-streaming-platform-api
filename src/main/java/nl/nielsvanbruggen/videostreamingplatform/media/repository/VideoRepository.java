package nl.nielsvanbruggen.videostreamingplatform.media.repository;

import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findAllByMedia(Media media);

    Optional<Video> findByPath(String path);

    Optional<Video> findFirstByMedia(Media media);

    @Query("SELECT COUNT(v) " +
            "FROM Video v " +
            "WHERE v.media = :media")
    int countByMedia(Media media);
}
