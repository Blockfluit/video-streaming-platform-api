package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import lombok.Builder;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTOSpecific;

@Data
@Builder
public class MediaGetResponse {
    MediaDTOSpecific media;
}
