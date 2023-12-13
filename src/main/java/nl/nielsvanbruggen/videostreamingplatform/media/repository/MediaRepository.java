package nl.nielsvanbruggen.videostreamingplatform.media.repository;

import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.watched.Watched;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {
    Optional<Media> findByName(String name);
    @Query("SELECT m1 " +
            "FROM Media m1 " +
            "INNER JOIN Video v1 ON m1 = v1.media " +
            "INNER JOIN Watched w1 ON v1 = w1.video " +
            "WHERE w1.updatedAt IN " +
            "(SELECT MAX(w2.updatedAt) " +
            "FROM Watched w2 " +
            "INNER JOIN Video v2 ON v2 = w2.video " +
            "INNER JOIN Media m2 ON m2 = v2.media " +
            "GROUP BY m2.id) " +
            "ORDER BY w1.updatedAt DESC")
    List<Media> findAllLastWatchedMedia(Pageable pageable);
}
