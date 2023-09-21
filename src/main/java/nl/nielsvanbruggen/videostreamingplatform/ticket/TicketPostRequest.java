package nl.nielsvanbruggen.videostreamingplatform.ticket;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TicketPostRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String comment;
    private Type type;
}
