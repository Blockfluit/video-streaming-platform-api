package nl.nielsvanbruggen.videostreamingplatform.ticket;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AllTicketsGetResponse {
    List<TicketDTO> allTickets;
}
