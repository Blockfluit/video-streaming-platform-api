package nl.nielsvanbruggen.videostreamingplatform.watchlist;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/watchlist")
public class WatchlistController {
    private final WatchlistService watchlistService;

    @GetMapping
    public ResponseEntity<WatchlistGetResponse> getWatchlist(Authentication authentication) {
        WatchlistGetResponse response = WatchlistGetResponse.builder()
                .watchlist(watchlistService.getWatchlist(authentication))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postWatchlist(@RequestBody WatchlistRequest watchlistRequest, Authentication authentication) {
        watchlistService.postWatchlist(watchlistRequest, authentication);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteWatchlist(@RequestBody WatchlistRequest watchlistRequest, Authentication authentication) {
        watchlistService.deleteWatchlist(watchlistRequest, authentication);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
