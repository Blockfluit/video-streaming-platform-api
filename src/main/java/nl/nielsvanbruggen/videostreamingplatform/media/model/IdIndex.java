package nl.nielsvanbruggen.videostreamingplatform.media.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IdIndex {
    @NotNull
    private Long id;
    @NotNull
    private Integer index;
}
