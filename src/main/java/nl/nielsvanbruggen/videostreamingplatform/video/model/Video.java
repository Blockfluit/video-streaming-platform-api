package nl.nielsvanbruggen.videostreamingplatform.video.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Video {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String path;
    @Column(name = "_index")
    private int index;
    private double duration;
    private int season;
    private String snapshot;
    @Column(name = "x_resolution")
    private Integer xResolution;
    @Column(name = "y_resolution")
    private Integer yResolution;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    private Media media;
}
