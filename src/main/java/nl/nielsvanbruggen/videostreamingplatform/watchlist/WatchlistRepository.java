package nl.nielsvanbruggen.videostreamingplatform.watchlist;

import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, WatchlistId> {
    List<Watchlist> findAllByUser(User user);
}
