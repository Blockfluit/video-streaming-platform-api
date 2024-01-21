package nl.nielsvanbruggen.videostreamingplatform.stream;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoTokenGetResponse {
    private final String token;
}
