package nl.nielsvanbruggen.videostreamingplatform.auth.model;

import jakarta.persistence.*;
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
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue
    private Long id;
    private String token;
    private Instant expiration;
    @Column(name = "created_at")
    private Instant createdAt;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
