package nl.nielsvanbruggen.videostreamingplatform.passwordreset;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PasswordResetToken {
    @Id
    private String token;
    private Instant createdAt = Instant.now();
    private Instant expiration;
    @ManyToOne
    private User user;
}
