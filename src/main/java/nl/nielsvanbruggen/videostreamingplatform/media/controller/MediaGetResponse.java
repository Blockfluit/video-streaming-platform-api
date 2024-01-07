package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import lombok.Builder;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTO;

@Data
@Builder
public class MediaGetResponse {
    MediaDTO media;
}
