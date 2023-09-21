package nl.nielsvanbruggen.videostreamingplatform.actor.controller;


import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.actor.model.Actor;
import nl.nielsvanbruggen.videostreamingplatform.actor.service.ActorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/actors")
@RequiredArgsConstructor
public class ActorController {
    private final ActorService actorService;

    @GetMapping
    public ResponseEntity<List<Actor>> getActor() {
        return new ResponseEntity<>(actorService.getActors(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> postActor(@RequestBody ActorPostRequest actorPostRequest) {
        actorService.postActor(actorPostRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteActor(@RequestBody long id) {
        actorService.deleteActor(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
