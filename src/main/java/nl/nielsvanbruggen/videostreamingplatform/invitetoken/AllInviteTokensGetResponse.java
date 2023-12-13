package nl.nielsvanbruggen.videostreamingplatform.invitetoken;

import lombok.Builder;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.ticket.TicketDTO;

import java.util.List;

@Data
@Builder
public class AllInviteTokensGetResponse {
    List<InviteTokenDTO> allInviteTokens;
}
