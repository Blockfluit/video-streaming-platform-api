package nl.nielsvanbruggen.videostreamingplatform.media.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

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
    @ManyToOne
    @JoinColumn(name = "media_id")
    private Media media;
}
