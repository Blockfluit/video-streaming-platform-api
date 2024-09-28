package nl.nielsvanbruggen.videostreamingplatform.media.model;

import jakarta.persistence.*;
import lombok.*;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

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
    private String imdbId;
    private Double imdbRating;
    private Long imdbRatingsAmount;
    private String name;
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
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
}
