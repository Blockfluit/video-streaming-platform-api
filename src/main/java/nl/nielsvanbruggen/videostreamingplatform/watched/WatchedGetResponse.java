package nl.nielsvanbruggen.videostreamingplatform.watched;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WatchedGetResponse {
    List<WatchedDTO> allWatched;
}
