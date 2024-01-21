package nl.nielsvanbruggen.videostreamingplatform.ticket;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.global.exception.ErrorInfo;
import nl.nielsvanbruggen.videostreamingplatform.global.exception.GlobalExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
public class TicketController {
    private final TicketService ticketService;

    @GetMapping
    public ResponseEntity<AllTicketsGetResponse> getAllTickets(Authentication authentication) {
        AllTicketsGetResponse response = AllTicketsGetResponse.builder()
                .allTickets(ticketService.getAllTickets(authentication))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
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
        ticketService.deleteTicket(ticketDeleteRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
