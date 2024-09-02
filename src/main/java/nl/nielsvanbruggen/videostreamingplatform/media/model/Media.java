package nl.nielsvanbruggen.videostreamingplatform.media.model;

import jakarta.persistence.*;
import lombok.*;
import nl.nielsvanbruggen.videostreamingplatform.actor.model.MediaActor;
import nl.nielsvanbruggen.videostreamingplatform.genre.MediaGenre;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Media {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private long id;
    private String name;
    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;
    private String thumbnail;
    private String trailer;
    @Column(columnDefinition = "TEXT")
    private String plot;
    private int year;
    @Enumerated(EnumType.STRING)
    private Type type;
    private boolean hidden;
    @OneToMany(mappedBy = "media", fetch = FetchType.LAZY)
    private List<Video> videos;
    @OneToMany(mappedBy = "media", fetch = FetchType.EAGER)
    private List<MediaGenre> genres;
    @OneToMany(mappedBy = "media", fetch = FetchType.EAGER)
    private List<MediaActor> actors;
    @OneToMany(mappedBy = "media", fetch = FetchType.LAZY)
    private List<Review> reviews;
    @OneToMany(mappedBy = "media", fetch = FetchType.LAZY)
    private List<Rating> ratings;
}
