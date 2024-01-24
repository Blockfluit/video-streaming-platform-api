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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    private Media media;
    @Id
    @ManyToOne
    @JoinColumn(name = "genre_name")
    private Genre genre;
}
