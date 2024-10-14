package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.media.enums.MediaRelationType;

@Data
public class MediaRelationPatchRequestDto {
    private long mediaFrom;
    private long mediaTo;
    private MediaRelationType type;
}
