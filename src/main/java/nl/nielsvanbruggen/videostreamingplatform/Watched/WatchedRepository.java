package nl.nielsvanbruggen.videostreamingplatform.Watched;

import nl.nielsvanbruggen.videostreamingplatform.media.model.Video;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchedRepository extends JpaRepository<Watched, WatchedId> {
    List<Watched> findAllByUser(User user);
    Optional<Watched> findByUserAndVideo(User user, Video video);
}
