package nl.nielsvanbruggen.videostreamingplatform.watchlist;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(WatchlistId.class)
public class Watchlist {
    @Id
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User user;
    @Id
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    private Media media;
    @Column(name = "created_at")
    private Instant createdAt;
}
