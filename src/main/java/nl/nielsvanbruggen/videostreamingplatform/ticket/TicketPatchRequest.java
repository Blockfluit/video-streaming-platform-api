package nl.nielsvanbruggen.videostreamingplatform.ticket;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TicketPatchRequest {
    @NotBlank
    long id;
    String response;
    @NotBlank
    boolean resolved;
}
