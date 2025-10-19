package nl.nielsvanbruggen.videostreamingplatform.invitetoken;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.exception.InvalidTokenException;
import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteTokenService {
    private final InviteTokenRepository inviteTokenRepository;

    public List<InviteToken> getAllInviteTokens() {
        return inviteTokenRepository.findAll();
    }

    public void createInviteToken(InviteTokenPostRequest request, User user) {
        if(request.getExpiration() == null ||
                request.getExpiration().isBefore(Instant.now())) {
            throw new IllegalArgumentException();
        }

        Role role = (request.getRole() == null ||
                !Arrays.asList(Role.values()).contains(request.getRole())) ?
                Role.USER: request.getRole();

        InviteToken token = InviteToken.builder()
                .token(UUID.randomUUID().toString())
                .expiration(request.getExpiration())
                .used(false)
                .master(request.isMaster())
                .createdBy(user)
                .role(role)
                .createdAt(Instant.now())
                .build();
        inviteTokenRepository.save(token);
    }

    public void deleteInviteToken(String token) {
        inviteTokenRepository.findById(token)
            .ifPresentOrElse(
                    (inviteTokenRepository::delete),
                    () -> {throw new InvalidTokenException();}
            );
    }
}
