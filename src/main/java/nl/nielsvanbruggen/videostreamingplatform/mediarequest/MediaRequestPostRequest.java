package nl.nielsvanbruggen.videostreamingplatform.mediarequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MediaRequestPostRequest {
    @NotBlank
    private String name;
    private int year;
    private String comment;
}
