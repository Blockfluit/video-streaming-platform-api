package nl.nielsvanbruggen.videostreamingplatform.watchlist;

import lombok.Builder;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTO;

import java.util.List;

@Data
@Builder
public class WatchlistGetResponse {
    List<MediaDTO> watchlist;
}
