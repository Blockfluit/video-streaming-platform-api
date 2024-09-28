package nl.nielsvanbruggen.videostreamingplatform.person.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.person.id.MediaPersonId;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(MediaPersonId.class)
public class MediaPerson {
    @Id
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    private Media media;
    @Id
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
    @Id
    @Column(name = "context_role")
    private ContextRole contextRole;
}
