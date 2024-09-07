package nl.nielsvanbruggen.videostreamingplatform.person.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.person.model.Person;
import nl.nielsvanbruggen.videostreamingplatform.person.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping
    public ResponseEntity<Map<String, List<Person>>> getAllPersons() {
        return ResponseEntity.ok(Map.of("allPersons", personService.getAllPersons()));
    }

    @PostMapping
    public ResponseEntity<String> postPerson(@RequestBody @Valid PersonPostRequest request) {
        personService.postPerson(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
