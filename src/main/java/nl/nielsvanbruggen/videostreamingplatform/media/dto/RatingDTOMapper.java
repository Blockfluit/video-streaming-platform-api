package nl.nielsvanbruggen.videostreamingplatform.media.dto;

import nl.nielsvanbruggen.videostreamingplatform.media.model.Rating;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RatingDTOMapper implements Function<Rating, RatingDTO> {
    @Override
    public RatingDTO apply(Rating rating) {
        return new RatingDTO(
                rating.getMedia().getId(),
                rating.getUser().getUsername(),
                rating.getScore()
        );
    }
}
