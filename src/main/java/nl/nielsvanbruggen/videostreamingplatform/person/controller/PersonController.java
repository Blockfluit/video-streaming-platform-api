package nl.nielsvanbruggen.videostreamingplatform.person.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.person.model.Person;
import nl.nielsvanbruggen.videostreamingplatform.person.service.PersonService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping("/")
    public ResponseEntity<Page<Person>> getPersons(@RequestParam int pagenumber,
                                                   @RequestParam int pagesize,
                                                   @RequestParam(required = false, defaultValue = "") String search) {
        return ResponseEntity.ok(personService.getPersons(pagenumber, pagesize, search));
    }

    @PostMapping("/")
    public ResponseEntity<String> postPerson(@RequestBody @Valid PersonPostRequest request) {
        personService.postPerson(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.ok().build();
    }
}
