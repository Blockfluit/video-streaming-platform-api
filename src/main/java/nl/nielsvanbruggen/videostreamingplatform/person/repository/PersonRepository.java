package nl.nielsvanbruggen.videostreamingplatform.person.repository;

import nl.nielsvanbruggen.videostreamingplatform.person.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByFirstnameAndLastname(String firstname, String lastname);
    Optional<Person> findByImdbId(String imdbId);
    List<Person> findByImdbIdIn(List<String> imdbIds);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE LOWER(CONCAT(p.firstname, ' ', p.lastname)) LIKE '%'|| LOWER(:search) || '%' " +
            "ORDER BY LOWER(CONCAT(p.firstname, ' ', p.lastname)) ASC")
    Page<Person> findAllByPartialName(String search, Pageable pageable);
}
