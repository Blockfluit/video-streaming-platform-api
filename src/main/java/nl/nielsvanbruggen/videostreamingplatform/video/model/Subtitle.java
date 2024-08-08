package nl.nielsvanbruggen.videostreamingplatform.video.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Subtitle {
    @Id
    @GeneratedValue
    long id;
    private String label;
    private String srcLang;
    private String path;
    private boolean defaultSub;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;
}
