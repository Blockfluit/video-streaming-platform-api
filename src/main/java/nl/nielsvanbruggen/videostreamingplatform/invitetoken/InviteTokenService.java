package nl.nielsvanbruggen.videostreamingplatform.invitetoken;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.global.exception.InvalidTokenException;
import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserRepository;
import nl.nielsvanbruggen.videostreamingplatform.global.util.TokenGeneratorUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class InviteTokenService {
    private final InviteTokenRepository inviteTokenRepository;
    private final UserRepository userRepository;

    public void createInviteToken(InviteTokenPostRequest inviteTokenPostRequest, Authentication authentication) {
        if(inviteTokenPostRequest.getExpiration() == null ||
                inviteTokenPostRequest.getExpiration().isBefore(Instant.now())) {
            throw new IllegalArgumentException();
        }

        Role role = (inviteTokenPostRequest.getRole() == null ||
                !Arrays.asList(Role.values()).contains(inviteTokenPostRequest.getRole())) ?
                Role.USER: inviteTokenPostRequest.getRole();

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow();

        InviteToken token = InviteToken.builder()
                .token(TokenGeneratorUtil.generate(64))
                .expiration(inviteTokenPostRequest.getExpiration())
                .used(false)
                .master(inviteTokenPostRequest.isMaster())
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
