package nl.nielsvanbruggen.videostreamingplatform.genre;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class MediaGenreId implements Serializable {
    private Media media;
    private Genre genre;
}
