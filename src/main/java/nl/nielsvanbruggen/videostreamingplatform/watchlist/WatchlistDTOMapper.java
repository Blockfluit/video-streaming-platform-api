package nl.nielsvanbruggen.videostreamingplatform.watchlist;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class WatchlistDTOMapper implements Function<Watchlist, WatchlistDTO> {
    @Override
    public WatchlistDTO apply(Watchlist watchlist) {
        return new WatchlistDTO(
                watchlist.getMedia().getId(),
                watchlist.getCreatedAt()
        );
    }
}
