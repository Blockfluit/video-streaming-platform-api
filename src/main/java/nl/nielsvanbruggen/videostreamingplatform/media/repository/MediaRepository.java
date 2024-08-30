package nl.nielsvanbruggen.videostreamingplatform.media.repository;

import nl.nielsvanbruggen.videostreamingplatform.genre.Genre;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {
    Optional<Media> findByName(String name);

    @Query("SELECT m " +
            "FROM Media m " +
            "LEFT JOIN MediaGenre g ON m = g.media " +
            "LEFT JOIN MediaActor ma ON m = ma.media " +
            "LEFT JOIN Actor a ON ma.actor = a " +
            "WHERE m.type LIKE '%'|| :type || '%' " +
            "AND g.genre IN :genres " +
            "AND (" +
            "   LOWER(m.name) LIKE '%'|| LOWER(:search) || '%' " +
            "   OR LOWER(g.genre.name) LIKE '%'|| LOWER(:search) || '%' " +
            "   OR LOWER(CONCAT(a.firstname, ' ', a.lastname)) LIKE '%'|| LOWER(:search) || '%' " +
            ") " +
            "AND m.hidden = false " +
            "GROUP BY m " +
            "ORDER BY m.updatedAt DESC")
    Page<Media> findAllByPartialNameTypeAndGenres(String search, String type, List<Genre> genres, Pageable pageable);

    @Query("SELECT m.id, m.name " +
            "FROM Media m " +
            "LEFT JOIN MediaGenre g ON m = g.media " +
            "WHERE m.type LIKE '%'|| :type || '%' " +
            "AND g.genre IN :genres " +
            "AND LOWER(m.name) LIKE LOWER(:search) || '%' " +
            "AND m.hidden = false " +
            "GROUP BY m " +
            "ORDER BY m.name ASC")
    Page<Media> findAutoCompletion(String search, String type, List<Genre> genres, Pageable pageable);

    @Query("SELECT m " +
            "FROM Media m " +
            "INNER JOIN Video v ON m = v.media " +
            "INNER JOIN Watched w ON v = w.video " +
            "WHERE m.type LIKE '%'|| :type || '%' " +
            "AND m.hidden = false " +
            "GROUP BY m.id " +
            "ORDER BY MAX(w.updatedAt) DESC")
    Page<Media> findAllLastWatchedByType(String type, Pageable pageable);

    @Query("SELECT m " +
            "FROM Media m " +
            "INNER JOIN Rating r ON m = r.media " +
            "WHERE m.type LIKE '%'|| :type || '%' " +
            "AND m.hidden = false " +
            "GROUP BY m.id " +
            "ORDER BY AVG(r.score) DESC, COUNT(r) DESC")
    Page<Media> findAllBestRatedByType(String type, Pageable pageable);

    @Query("SELECT m " +
            "FROM Media m " +
            "INNER JOIN Video v ON m = v.media " +
            "INNER JOIN Watched w ON v = w.video " +
            "WHERE m.type LIKE '%'|| :type || '%' " +
            "AND m.hidden = false " +
            "GROUP BY m.id " +
            "ORDER BY COUNT(DISTINCT w.user) DESC")
    Page<Media> findAllMostWatchedByType(String type, Pageable pageable);

    @Query("SELECT m " +
            "FROM Media m " +
            "INNER JOIN Video v ON m = v.media " +
            "INNER JOIN Watched w ON v = w.video " +
            "WHERE w.user = :user " +
            "AND m.type LIKE '%'|| :type || '%' " +
            "GROUP BY m.id " +
            "ORDER BY MAX(w.updatedAt) DESC")
    Page<Media> findRecentWatchedByUserAndType(User user, String type, Pageable pageable);

    @Query("SELECT m " +
            "FROM Media m " +
            "INNER JOIN Video v ON m = v.media " +
            "INNER JOIN Watched w ON v = w.video " +
            "WHERE w.user = :user " +
            "AND m.type LIKE '%'|| :type || '%' " +
            "GROUP BY m.id " +
            "ORDER BY MAX(w.updatedAt) DESC")
    Page<Media> findAllRecentWatchedByUserAndType(User user, String type, Pageable pageable);

    @Query("SELECT m " +
            "FROM Media m " +
            "WHERE m.type LIKE '%'|| :type || '%' " +
            "AND m.updatedAt > :threshold " +
            "AND m.hidden = false " +
            "ORDER BY m.updatedAt DESC")
    Page<Media> findAllRecentUploadedByType(String type, Instant threshold, Pageable pageable);

    @Query("SELECT m " +
            "FROM Media m " +
            "INNER JOIN Video v ON m = v.media " +
            "LEFT JOIN Watched w ON v = w.video " +
            "AND w.user = :user " +
            "AND m.hidden = false " +
            "WHERE w IS NULL " +
            "GROUP BY m")
    List<Media> findAllNotWatchedByUser(User user);
}
