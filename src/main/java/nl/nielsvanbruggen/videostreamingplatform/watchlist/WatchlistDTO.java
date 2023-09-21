package nl.nielsvanbruggen.videostreamingplatform.watchlist;

import java.time.Instant;

public record WatchlistDTO(
        long MediaId,
        Instant createdAt
) {
}
