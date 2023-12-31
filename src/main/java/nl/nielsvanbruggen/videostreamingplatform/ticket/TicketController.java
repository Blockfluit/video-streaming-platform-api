package nl.nielsvanbruggen.videostreamingplatform.ticket;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.global.exception.GlobalExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
public class TicketController {
    private final TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getTickets(Authentication authentication) {
        return new ResponseEntity<>(ticketService.getTickets(authentication), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createTicket(@Valid @RequestBody TicketPostRequest ticketPostRequest, Authentication authentication) {
        ticketService.createTicket(ticketPostRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<String> patchTicket(@Valid @RequestBody TicketPatchRequest ticketPatchRequest) {
        ticketService.patchTicket(ticketPatchRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTicket(@Valid @RequestBody TicketDeleteRequest ticketDeleteRequest, Authentication authentication) {
        try {
            ticketService.deleteTicket(ticketDeleteRequest, authentication);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TicketException ex) {
            return new ResponseEntity<>(GlobalExceptionHandler.singleMessageToErrorMap(ex.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }
}
