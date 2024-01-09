package nl.nielsvanbruggen.videostreamingplatform.mediarequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MediaRequestRepository extends JpaRepository<MediaRequest, Long> {

    @Query("SELECT m " +
    "FROM MediaRequest m " +
    "WHERE LOWER(m.name) LIKE '%'|| LOWER(:search) || '%' " +
    "ORDER BY m.updatedAt DESC")
    Page<MediaRequest> findAll(String search, Pageable pageable);

}
