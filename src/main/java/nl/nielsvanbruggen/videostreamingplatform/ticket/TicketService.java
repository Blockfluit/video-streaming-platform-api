package nl.nielsvanbruggen.videostreamingplatform.ticket;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketDTOMapper ticketDTOMapper;

    public List<TicketDTO> getTickets(Authentication authentication) {
        if(authentication.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()))) {
            return ticketRepository.findAll().stream()
                    .map(ticketDTOMapper)
                    .collect(Collectors.toList());
        }
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow();

        return ticketRepository.findAllByCreatedBy(user).stream()
                .map(ticketDTOMapper)
                .collect(Collectors.toList());
    }

    public TicketDTO createTicket(TicketPostRequest request, Authentication authentication) {
        if(request.getType() == null) {
            throw new IllegalArgumentException("Type missing.");
        }

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow();

        Ticket ticket = Ticket.builder()
                .title(request.getTitle())
                .comment(request.getComment())
                .createdAt(Instant.now())
                .type(request.getType())
                .resolved(false)
                .createdBy(user)
                .build();
        ticketRepository.save(ticket);
        return ticketDTOMapper.apply(ticket);
    }

    public void patchTicket(TicketPatchRequest request) {
        Ticket ticket = ticketRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Id does not exist."));
        ticket.setResolved(request.isResolved());
        ticket.setResponse(request.getResponse());

        ticketRepository.save(ticket);
    }

    public void deleteTicket(TicketDeleteRequest request, Authentication authentication) {
        Ticket ticket = ticketRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Id does not exist."));

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow();
        if(!ticket.getCreatedBy().equals(user) &&
                !authentication.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()))) {
            throw new TicketException("Insufficient permission.");
        }

        ticketRepository.delete(ticket);
    }
}