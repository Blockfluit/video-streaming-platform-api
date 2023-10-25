package nl.nielsvanbruggen.videostreamingplatform.media.dto;

import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;

public record UserReviewDTO(
        String username,
        Role role
) {

}
