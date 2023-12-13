package nl.nielsvanbruggen.videostreamingplatform.actor.repository;

import nl.nielsvanbruggen.videostreamingplatform.actor.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActorRepository extends JpaRepository<Actor, Long> {

    Optional<Actor> findByFirstnameAndLastname(String firstname, String lastname);
}
