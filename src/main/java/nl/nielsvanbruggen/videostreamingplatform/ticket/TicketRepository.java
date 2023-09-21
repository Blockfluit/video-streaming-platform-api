package nl.nielsvanbruggen.videostreamingplatform.ticket;

import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByCreatedBy(User user);
}
