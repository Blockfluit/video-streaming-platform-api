package nl.nielsvanbruggen.videostreamingplatform.watched;

import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Video;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import org.hibernate.query.spi.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WatchedRepository extends JpaRepository<Watched, WatchedId> {
    List<Watched> findAllByUser(User user);
    Optional<Watched> findByUserAndVideo(User user, Video video);
    @Query("SELECT COUNT(DISTINCT user.id) " +
            "FROM Watched w " +
            "INNER JOIN Video v ON w.video.id = v.id " +
            "INNER JOIN Media m ON v.media.id = m.id " +
            "WHERE m = :media")
    int totalUniqueViewsByMedia(@Param("media") Media media);
    @Query("SELECT w " +
            "FROM Watched w " +
            "WHERE w.user = :user " +
            "ORDER BY w.updatedAt DESC")
    List<Watched> findLastWatchedByUser(@Param("user") User user, Pageable pageable);

    List<Watched> deleteByVideoIn(List<Video> videos);

    @Query("SELECT DISTINCT v.media " +
            "FROM Watched w " +
            "INNER JOIN Video v ON w.video = v " +
            "WHERE w.user = :user")
    List<Media> findAllMediaByUser(@Param("user") User user);
}
