package nl.nielsvanbruggen.videostreamingplatform.media.dto;

import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserReviewDTOMapper implements Function<User, UserReviewDTO> {
    @Override
    public UserReviewDTO apply(User user) {
        return new UserReviewDTO(
                user.getUsername(),
                user.getRole()
        );
    }
}
