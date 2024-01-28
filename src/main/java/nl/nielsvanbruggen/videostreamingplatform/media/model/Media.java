package nl.nielsvanbruggen.videostreamingplatform.media.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.actor.model.MediaActor;
import nl.nielsvanbruggen.videostreamingplatform.genre.MediaGenre;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

    @Override
    public boolean equals(Object object) {
        if(object == null) return false;
        if(!(object instanceof Media media)) return false;
        return media.getId() == this.getId();
    }
}
