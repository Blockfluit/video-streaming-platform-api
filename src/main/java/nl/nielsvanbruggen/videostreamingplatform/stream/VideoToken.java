package nl.nielsvanbruggen.videostreamingplatform.stream;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "video_token", uniqueConstraints={
        @UniqueConstraint(columnNames = {"video_id", "user_id"})
})
public class VideoToken {
    private final static int VIDEO_EXPIRATION_IN_MINUTES = 60;

    @Id
    @GeneratedValue
    private UUID token;
    private Instant createdAt;
    @Builder.Default
    private Instant expiration = getNewExpiration();
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public boolean isValid(Video video) {
        return this.video.equals(video) && Instant.now().isBefore(expiration);
    }

    public void resetExpiration() {
        this.expiration = getNewExpiration();
    }

    private static Instant getNewExpiration() {
        return Instant.now().plus(VIDEO_EXPIRATION_IN_MINUTES, ChronoUnit.MINUTES);
    }
}
