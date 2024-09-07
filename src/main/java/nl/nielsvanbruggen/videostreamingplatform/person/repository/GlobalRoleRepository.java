package nl.nielsvanbruggen.videostreamingplatform.person.repository;

import nl.nielsvanbruggen.videostreamingplatform.person.model.GlobalRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalRoleRepository extends JpaRepository<GlobalRole, String> {
}
