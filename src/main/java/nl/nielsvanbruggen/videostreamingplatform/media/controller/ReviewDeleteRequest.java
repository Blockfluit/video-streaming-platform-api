package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewDeleteRequest {
    @NotNull
    private Long id;
}
