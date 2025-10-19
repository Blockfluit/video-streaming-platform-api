package nl.nielsvanbruggen.videostreamingplatform.media.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "media")
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
    private MediaType type;
    private boolean hidden;
}
