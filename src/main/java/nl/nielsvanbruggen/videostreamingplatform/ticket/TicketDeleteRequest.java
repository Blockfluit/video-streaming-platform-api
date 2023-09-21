package nl.nielsvanbruggen.videostreamingplatform.ticket;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TicketDeleteRequest {
    @NotBlank
    private Long id;
}
