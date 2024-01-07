package nl.nielsvanbruggen.videostreamingplatform.media.repository;

import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {
    @Query("SELECT m " +
            "FROM Media m " +
            "WHERE LOWER(m.name) LIKE '%'|| LOWER(:search) || '%' " +
            "ORDER BY m.updatedAt DESC")
    Page<Media> findAllByPartialName(String search, Pageable pageable);

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
    Page<Media> findAllLastWatched(Pageable pageable);

    @Query("SELECT m " +
            "FROM Media m " +
            "INNER JOIN Rating r " +
            "ON m = r.media " +
            "GROUP BY m.id " +
            "ORDER BY AVG(r.score) DESC, COUNT(r.media) DESC")
    Page<Media> findAllBestRated(Pageable pageable);

    @Query("SELECT m " +
            "FROM Media m " +
            "INNER JOIN Video v " +
            "ON m = v.media " +
            "INNER JOIN Watched w " +
            "ON v = w.video " +
            "GROUP BY m.id " +
            "ORDER BY COUNT(DISTINCT w.user) DESC")
    Page<Media> findAllMostWatched(Pageable pageable);
}
