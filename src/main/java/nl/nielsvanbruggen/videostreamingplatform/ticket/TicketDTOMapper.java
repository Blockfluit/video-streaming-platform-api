package nl.nielsvanbruggen.videostreamingplatform.ticket;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TicketDTOMapper implements Function<Ticket, TicketDTO> {
    @Override
    public TicketDTO apply(Ticket ticket) {
        return new TicketDTO(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getComment(),
                ticket.getCreatedAt(),
                ticket.getType(),
                ticket.isResolved(),
                ticket.getCreatedBy().getUsername()
        );
    }
}
