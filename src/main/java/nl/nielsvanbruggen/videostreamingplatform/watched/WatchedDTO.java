package nl.nielsvanbruggen.videostreamingplatform.watched;

import java.time.Instant;

public record WatchedDTO(
        long videoId,
        String name,
        int index,
        int season,
        float timestamp,
        long mediaId,
        Instant updatedAt,
        double duration
) {
}
