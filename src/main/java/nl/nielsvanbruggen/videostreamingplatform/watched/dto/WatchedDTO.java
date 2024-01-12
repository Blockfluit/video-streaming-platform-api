package nl.nielsvanbruggen.videostreamingplatform.watched.dto;

import java.time.Instant;

public record WatchedDTO(
        long videoId,
        String name,
        int index,
        int season,
        double timestamp,
        long mediaId,
        Instant updatedAt,
        double duration
) {
}
