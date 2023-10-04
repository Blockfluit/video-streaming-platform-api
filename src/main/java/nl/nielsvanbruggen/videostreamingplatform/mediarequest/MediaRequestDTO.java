package nl.nielsvanbruggen.videostreamingplatform.mediarequest;

import java.time.Instant;

public record MediaRequestDTO(
        long id,
        String name,
        int year,
        String comment,
        Instant createdAt,
        String createdBy,
        Status status
) { }
