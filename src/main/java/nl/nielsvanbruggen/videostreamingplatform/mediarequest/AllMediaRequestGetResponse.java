package nl.nielsvanbruggen.videostreamingplatform.mediarequest;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AllMediaRequestGetResponse {
    List<MediaRequestDTO> allMediaRequests;
}
