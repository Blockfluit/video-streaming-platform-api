package nl.nielsvanbruggen.videostreamingplatform.person.repository;

import nl.nielsvanbruggen.videostreamingplatform.person.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByFirstnameAndLastname(String firstname, String lastname);
    Optional<Person> findByImdbId(String imdbId);
    List<Person> findByImdbIdIn(List<String> imdbIds);
}
