package nl.nielsvanbruggen.videostreamingplatform.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserActivityId;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UserActivityId.class)
public class UserActivity {
    @Id
    @Column(name = "created_at")
    private Instant createdAt;
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}