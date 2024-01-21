package nl.nielsvanbruggen.videostreamingplatform.user.dto;

import lombok.Builder;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.auth.repository.RefreshTokenWithoutUser;
import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;
import nl.nielsvanbruggen.videostreamingplatform.watched.dto.WatchedDTO;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class UserDTO {
        private long id;
        private String username;
        private Role role;
        private Instant lastActiveAt;
        private Instant lastLoginAt;
        private Instant createdAt;
        private List<WatchedDTO> lastWatched;
        private List<RefreshTokenWithoutUser> refreshTokens;

}
