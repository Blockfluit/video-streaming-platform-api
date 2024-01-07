package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import lombok.Builder;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTO;

import java.util.List;

@Data
@Builder
public class MediaListGetResponse {
    List<MediaDTO> content;
}
