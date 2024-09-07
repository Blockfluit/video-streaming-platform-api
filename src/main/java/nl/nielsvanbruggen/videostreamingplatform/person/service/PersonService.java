package nl.nielsvanbruggen.videostreamingplatform.person.service;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.person.controller.PersonPostRequest;
import nl.nielsvanbruggen.videostreamingplatform.person.model.Person;
import nl.nielsvanbruggen.videostreamingplatform.person.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    /**
     * Saves all provided persons. If person already exist it will override all fields that are not null.
     * */
    public List<Person> saveAll(Collection<Person> persons) {
        List<String> imdbIds = persons.stream()
                .map(Person::getImdbId)
                .filter(Objects::nonNull)
                .toList();

        Map<String, Person> dbPersonLookup = personRepository.findByImdbIdIn(imdbIds).stream()
                .collect(Collectors.toMap(
                        Person::getImdbId,
                        Function.identity()
                ));

        return personRepository.saveAll(
                persons.stream()
                        .map(person -> {
                            Person dbPerson = dbPersonLookup.get(person.getImdbId());
                            if(dbPerson == null) return person;

                            if(person.getFirstname() != null) dbPerson.setFirstname(person.getFirstname());
                            if(person.getLastname() != null) dbPerson.setLastname(person.getLastname());
                            if(person.getDescription() != null) dbPerson.setDescription(person.getDescription());
                            if(person.getDateOfBirth() != null) dbPerson.setDateOfBirth(person.getDateOfBirth());
                            if(person.getDateOfDeath() != null) dbPerson.setDateOfDeath(person.getDateOfDeath());

                            return dbPerson;
                        })
                        .toList()
        );
    }

    public void postPerson(PersonPostRequest request) {
        personRepository.findByFirstnameAndLastname(request.getFirstname(), request.getLastname())
                .ifPresent((actor) -> {
                    throw new IllegalArgumentException("actor already exists");
                });

        personRepository.save(
                Person.builder()
                        .imdbId(request.getImdbId())
                        .firstname(request.getFirstname())
                        .lastname(request.getLastname())
                        .description(request.getDescription())
                        .dateOfBirth(request.getDateOfBirth())
                        .dateOfDeath(request.getDateOfDeath())
                        .build()
        );
    }

    public void deletePerson(long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Actor does not exist."));

        personRepository.delete(person);
    }
}
