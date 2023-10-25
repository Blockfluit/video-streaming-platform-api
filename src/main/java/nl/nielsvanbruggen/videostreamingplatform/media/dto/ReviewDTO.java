package nl.nielsvanbruggen.videostreamingplatform.media.dto;

import java.time.Instant;

public record ReviewDTO(
        long id,
        String title,
        String comment,
        UserReviewDTO user,
        Instant createdAt,
        Instant updatedAt
) {
}
