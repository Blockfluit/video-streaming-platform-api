package nl.nielsvanbruggen.videostreamingplatform.recommendation;

import lombok.Builder;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTO;

import java.util.List;

@Data
@Builder
public class Recommendation {
    private List<MediaDTO> content;
    private List<MediaDTO> input;
    private int threshold;
}
