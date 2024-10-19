package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.media.enums.MediaRelationType;

@Data
public class MediaRelationPostRequestDto {
    @NotNull
    private Long mediaFrom;
    @NotNull
    private Long mediaTo;
    @NotNull
    private MediaRelationType type;
}
