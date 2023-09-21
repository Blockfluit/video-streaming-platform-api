package nl.nielsvanbruggen.videostreamingplatform.media.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.id.RatingId;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(RatingId.class)
public class Rating {
    @Id
    @ManyToOne
    @JoinColumn(name = "media_id")
    private Media media;
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private double score;
}
