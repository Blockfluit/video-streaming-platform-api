package nl.nielsvanbruggen.videostreamingplatform.media.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.enums.MediaRelationType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "media_relation")
public class MediaRelation {
    @Id
    @GeneratedValue
    private long id;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "media_from")
    private Media mediaFrom;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "media_to")
    private Media mediaTo;
    @Enumerated(EnumType.STRING)
    private MediaRelationType type;
}
