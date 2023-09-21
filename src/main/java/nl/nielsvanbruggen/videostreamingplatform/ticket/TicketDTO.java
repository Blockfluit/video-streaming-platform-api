package nl.nielsvanbruggen.videostreamingplatform.ticket;

import java.time.Instant;

public record TicketDTO(
        long id,
        String title,
        String comment,
        Instant createdAt,
        Type type,
        boolean resolved,
        String createdBy) {

}
