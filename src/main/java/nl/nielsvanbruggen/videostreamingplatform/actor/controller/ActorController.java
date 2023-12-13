package nl.nielsvanbruggen.videostreamingplatform.actor.controller;


import jakarta.validation.Valid;
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
    public ResponseEntity<AllActorsGetResponse> getAllActor() {
        AllActorsGetResponse response = AllActorsGetResponse.builder()
                .allActors(actorService.getActors())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> postActor(@RequestBody @Valid ActorPostRequest actorPostRequest) {
        actorService.postActor(actorPostRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteActor(@PathVariable Long id) {
        actorService.deleteActor(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
