package nl.nielsvanbruggen.videostreamingplatform.watchlist;

import lombok.Builder;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTOGeneral;

import java.util.List;

@Data
@Builder
public class WatchlistGetResponse {
    List<MediaDTOGeneral> watchlist;
}
