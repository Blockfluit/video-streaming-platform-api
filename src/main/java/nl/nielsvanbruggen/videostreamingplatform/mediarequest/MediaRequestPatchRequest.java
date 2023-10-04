package nl.nielsvanbruggen.videostreamingplatform.mediarequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MediaRequestPatchRequest {
    private String name;
    private Integer year;
    private String comment;
    private Status status;
}
