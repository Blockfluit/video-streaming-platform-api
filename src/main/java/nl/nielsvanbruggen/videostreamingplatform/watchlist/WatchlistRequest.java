package nl.nielsvanbruggen.videostreamingplatform.watchlist;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WatchlistRequest {
    @NotBlank
    long id;
}
