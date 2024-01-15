package nl.nielsvanbruggen.videostreamingplatform.video.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;
}
