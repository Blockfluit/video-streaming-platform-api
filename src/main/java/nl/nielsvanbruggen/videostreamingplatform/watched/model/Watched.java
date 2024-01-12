package nl.nielsvanbruggen.videostreamingplatform.watched.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Video;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.watched.id.WatchedId;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(WatchedId.class)
public class Watched {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Id
    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;
    private double timestamp;
    @Column(name = "updated_at")
    private Instant updatedAt;
    @Column(name = "created_at")
    private Instant createdAt;
}
