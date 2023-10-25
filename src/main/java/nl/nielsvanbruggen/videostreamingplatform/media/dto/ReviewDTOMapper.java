package nl.nielsvanbruggen.videostreamingplatform.media.dto;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Review;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ReviewDTOMapper implements Function<Review, ReviewDTO> {
    private final UserReviewDTOMapper userReviewDTOMapper;

    @Override
    public ReviewDTO apply(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getTitle(),
                review.getComment(),
                userReviewDTOMapper.apply(review.getUser()),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
