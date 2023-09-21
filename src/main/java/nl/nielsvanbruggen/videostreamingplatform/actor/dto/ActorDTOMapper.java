package nl.nielsvanbruggen.videostreamingplatform.actor.dto;

import nl.nielsvanbruggen.videostreamingplatform.actor.model.Actor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ActorDTOMapper implements Function<Actor, ActorDTO> {
    @Override
    public ActorDTO apply(Actor actor) {
        return new ActorDTO(
                actor.getFirstname(),
                actor.getLastname()
                );
    }
}
