package nl.nielsvanbruggen.videostreamingplatform.actor.service;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.actor.controller.ActorPostRequest;
import nl.nielsvanbruggen.videostreamingplatform.actor.model.Actor;
import nl.nielsvanbruggen.videostreamingplatform.actor.repository.ActorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActorService {
    private final ActorRepository actorRepository;

    public List<Actor> getActors() {
        return actorRepository.findAll();
    }

    public void postActor(String firstname, String lastname) {
        actorRepository.findByFirstnameAndLastname(firstname, lastname)
                .ifPresent((actor) -> {
                    throw new IllegalArgumentException("actor already exists");
                });

        Actor actor = Actor.builder()
                .firstname(firstname)
                .lastname(lastname)
                .build();
        actorRepository.save(actor);
    }

    public void deleteActor(long id) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Actor does not exist."));
        actorRepository.delete(actor);
    }
}
