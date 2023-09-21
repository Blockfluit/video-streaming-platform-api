package nl.nielsvanbruggen.videostreamingplatform.media.id;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class RatingId implements Serializable {
    private Media media;
    private User user;
}
