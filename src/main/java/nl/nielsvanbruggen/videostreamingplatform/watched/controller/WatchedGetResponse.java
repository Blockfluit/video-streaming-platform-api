package nl.nielsvanbruggen.videostreamingplatform.watched.controller;

import lombok.Builder;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.watched.dto.WatchedDTO;

import java.util.List;

@Data
@Builder
public class WatchedGetResponse {
    List<WatchedDTO> allWatched;
}
