package nl.nielsvanbruggen.videostreamingplatform.invitetoken;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class InviteTokenDTOMapper implements Function<InviteToken, InviteTokenDTO> {
    @Override
    public InviteTokenDTO apply(InviteToken inviteToken) {
        return InviteTokenDTO.builder()
                .token(inviteToken.getToken())
                .role(inviteToken.getRole())
                .used(inviteToken.isUsed())
                .master(inviteToken.isMaster())
                .expiration(inviteToken.getExpiration())
                .createdAt(inviteToken.getCreatedAt())
                .createdBy(inviteToken.getCreatedBy() != null ?
                        inviteToken.getCreatedBy().getUsername() :
                        null)
                .build();
    }
}
