package nl.nielsvanbruggen.videostreamingplatform.Watched;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WatchedPostRequest {
    @NotBlank
    long id;
    @NotNull
    Float timestamp;
}