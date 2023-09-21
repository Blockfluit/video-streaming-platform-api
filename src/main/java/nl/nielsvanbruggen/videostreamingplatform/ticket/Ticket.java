package nl.nielsvanbruggen.videostreamingplatform.ticket;

import jakarta.persistence.*;
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
public class Ticket {
    @Id
    @GeneratedValue
    private long id;
    private String title;
    private String comment;
    @Enumerated(EnumType.STRING)
    private Type type;
    private boolean resolved;
    private String response;
    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "created_by")
    private User createdBy;
}
