package nl.nielsvanbruggen.videostreamingplatform.media.dto;

public record RatingDTO(
        long mediaId,
        String username,
        double score
) {
}
