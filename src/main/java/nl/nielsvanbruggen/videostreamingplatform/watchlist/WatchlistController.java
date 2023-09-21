package nl.nielsvanbruggen.videostreamingplatform.watchlist;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/watchlist")
public class WatchlistController {
    private final WatchlistService watchlistService;

    @GetMapping
    public ResponseEntity<List<WatchlistDTO>> getWatchlist(Authentication authentication) {
        return new ResponseEntity<>(watchlistService.getWatchlist(authentication), HttpStatus.OK);
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
