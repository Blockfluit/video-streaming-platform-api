package nl.nielsvanbruggen.videostreamingplatform.Watched;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Video;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class WatchedId implements Serializable {
    private User user;
    private Video video;
}
