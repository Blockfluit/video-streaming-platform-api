package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewPostRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String comment;
}
