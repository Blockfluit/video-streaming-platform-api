package nl.nielsvanbruggen.videostreamingplatform.invitetoken;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class InviteTokenDTOMapper implements Function<InviteToken, InviteTokenDTO> {
    @Override
    public InviteTokenDTO apply(InviteToken inviteToken) {
        return new InviteTokenDTO(
                inviteToken.getToken(),
                inviteToken.getExpiration(),
                inviteToken.isUsed(),
                inviteToken.isMaster(),
                inviteToken.getCreatedAt(),
                inviteToken.getRole(),
                inviteToken.getCreatedBy().getUsername());
    }
}
