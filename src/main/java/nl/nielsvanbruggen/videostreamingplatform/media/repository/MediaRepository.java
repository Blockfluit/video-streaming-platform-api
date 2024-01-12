package nl.nielsvanbruggen.videostreamingplatform.media.repository;

import nl.nielsvanbruggen.videostreamingplatform.genre.Genre;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Type;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {
    Optional<Media> findByName(String name);

    @Query("SELECT m " +
            "FROM Media m " +
            "INNER JOIN MediaGenre g ON m = g.media " +
            "WHERE m.type LIKE '%'|| :type || '%' " +
            "AND LOWER(m.name) LIKE '%'|| LOWER(:search) || '%' " +
            "AND g.genre IN :genres " +
            "GROUP BY m " +
            "ORDER BY m.updatedAt DESC")
    Page<Media> findAllByPartialName(String search, String type, List<Genre> genres, Pageable pageable);

    @Query("SELECT m " +
            "FROM Media m " +
            "INNER JOIN Video v ON m = v.media " +
            "INNER JOIN Watched w ON v = w.video " +
            "WHERE m.type LIKE '%'|| :type || '%' " +
            "GROUP BY m.id " +
            "ORDER BY MAX(w.updatedAt) DESC")
    Page<Media> findAllLastWatched(String type, Pageable pageable);

    @Query("SELECT m " +
            "FROM Media m " +
            "INNER JOIN Rating r ON m = r.media " +
            "WHERE m.type LIKE '%'|| :type || '%' " +
            "GROUP BY m.id " +
            "ORDER BY AVG(r.score) DESC, COUNT(r) DESC")
    Page<Media> findAllBestRated(String type, Pageable pageable);

    @Query("SELECT m " +
            "FROM Media m " +
            "INNER JOIN Video v ON m = v.media " +
            "INNER JOIN Watched w ON v = w.video " +
            "WHERE m.type LIKE '%'|| :type || '%' " +
            "GROUP BY m.id " +
            "ORDER BY COUNT(DISTINCT w.user) DESC")
    Page<Media> findAllMostWatched(String type, Pageable pageable);

    @Query("SELECT m " +
            "FROM Media m " +
            "INNER JOIN Video v ON m = v.media " +
            "INNER JOIN Watched w ON v = w.video " +
            "WHERE w.user = :user " +
            "AND m.type LIKE '%'|| :type || '%' " +
            "GROUP BY m.id " +
            "HAVING MAX(w.timestamp / v.duration) < 0.98 " +
            "ORDER BY MAX(w.updatedAt) DESC")
    Page<Media> findAllRecentWatched(User user, String type, Pageable pageable);

    @Query("SELECT m " +
            "FROM Media m " +
            "WHERE m.type LIKE '%'|| :type || '%' " +
            "AND m.updatedAt > :threshold " +
            "ORDER BY m.updatedAt DESC")
    Page<Media> findAllRecentUploaded(String type, Instant threshold, Pageable pageable);
}
