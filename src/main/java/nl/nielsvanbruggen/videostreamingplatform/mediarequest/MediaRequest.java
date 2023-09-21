package nl.nielsvanbruggen.videostreamingplatform.mediarequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MediaRequest {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private int year;
    private String comment;
    private Status status;
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();
    @Column(name = "created_by")
    private User createdBy;
}
