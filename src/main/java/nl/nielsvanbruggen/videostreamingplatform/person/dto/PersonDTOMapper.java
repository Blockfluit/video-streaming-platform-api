package nl.nielsvanbruggen.videostreamingplatform.person.dto;

import nl.nielsvanbruggen.videostreamingplatform.person.model.Person;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PersonDTOMapper implements Function<Person, PersonDTO> {
    @Override
    public PersonDTO apply(Person person) {
        return PersonDTO.builder()
                .firstname(person.getFirstname())
                .lastname(person.getLastname())
                .build();
    }
}
