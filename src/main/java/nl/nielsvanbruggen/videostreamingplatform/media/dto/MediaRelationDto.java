package nl.nielsvanbruggen.videostreamingplatform.media.dto;

import lombok.Builder;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.media.enums.MediaRelationType;

@Data
@Builder
public class MediaRelationDto {
    private long id;
    private long mediaFrom;
    private long mediaTo;
    private MediaRelationType type;
}
