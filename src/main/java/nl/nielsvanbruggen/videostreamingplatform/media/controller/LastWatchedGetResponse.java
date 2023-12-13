package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import lombok.Builder;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTOGeneral;

import java.util.List;

@Data
@Builder
public class LastWatchedGetResponse {
    List<MediaDTOGeneral> lastWatched;
}
