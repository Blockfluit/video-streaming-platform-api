package nl.nielsvanbruggen.videostreamingplatform.media.repository;

import nl.nielsvanbruggen.videostreamingplatform.media.id.RatingId;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {
    List<Rating> findAllByMedia(Media media);
    List<Rating> deleteByMedia(Media media);

    @Query("SELECT AVG(r.score) " +
            "FROM Rating r " +
            "WHERE r.media = :media " +
            "GROUP BY r.media")
    Optional<Double> averageScoreByMedia(@Param("media") Media media);
}
