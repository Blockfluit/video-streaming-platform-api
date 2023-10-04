package nl.nielsvanbruggen.videostreamingplatform.Watched;

import java.time.Instant;

public record WatchedDTO(
        long videoId,
        String name,
        int season,
        float timestamp,
        long mediaId,
        Instant updatedAt,
        double duration
) {
}
