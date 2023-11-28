package nl.nielsvanbruggen.videostreamingplatform.watched;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WatchedPostRequest {
    Long id;
    @NotNull
    Float timestamp;
}
