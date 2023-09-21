package nl.nielsvanbruggen.videostreamingplatform.genre;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(MediaGenreId.class)
public class MediaGenre {
    @Id
    @ManyToOne
    private Media media;
    @Id
    @ManyToOne
    private Genre genre;
}
