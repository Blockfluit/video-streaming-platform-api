package nl.nielsvanbruggen.videostreamingplatform.media.repository;

import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByMedia(Media media);
}
