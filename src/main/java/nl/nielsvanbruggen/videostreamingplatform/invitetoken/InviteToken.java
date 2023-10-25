package nl.nielsvanbruggen.videostreamingplatform.invitetoken;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class InviteToken {
    @Id
    private String token;
    private Instant expiration;
    @Column(name = "used")
    private boolean used;
    private boolean master;
    @Column(name = "created_at")
    private Instant createdAt;
    @Enumerated(EnumType.STRING)
    private Role role;
    @JoinColumn(name = "created_by")
    @ManyToOne
    private User createdBy;
}
