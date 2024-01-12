package nl.nielsvanbruggen.videostreamingplatform.user.repository;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@Embeddable
public class UserActivityId implements Serializable {
    private Instant createdAt;
    private User user;
}
