package nl.nielsvanbruggen.videostreamingplatform.stream;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "video_token")
public class VideoToken {
    @Id
    @GeneratedValue
    private Long id;
    private String token;
    private Instant createdAt;
    private Instant expiration;
    @OneToOne
    @JoinColumn(name = "video_id")
    private Video video;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
