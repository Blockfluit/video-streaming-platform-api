package nl.nielsvanbruggen.videostreamingplatform.recommendation;

import lombok.Builder;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTOGeneral;

import java.util.List;

@Data
@Builder
public class RecommendationGetResponse {
    List<MediaDTOGeneral> recommendations;
}
