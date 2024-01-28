package nl.nielsvanbruggen.videostreamingplatform.watchlist;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTO;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.service.MediaService;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/watchlist")
public class WatchlistController {
    private final WatchlistService watchlistService;
    private final UserService userService;
    private final MediaService mediaService;

    @GetMapping
    public ResponseEntity<WatchlistGetResponse> getWatchlist(Authentication authentication) {
        User user = userService.getUser(authentication.getName());

        WatchlistGetResponse response = WatchlistGetResponse.builder()
                .watchlist(watchlistService.getWatchlist(user))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postWatchlist(@RequestBody WatchlistRequest watchlistRequest, Authentication authentication) {
        User user = userService.getUser(authentication.getName());
        Media media = mediaService.getMedia(watchlistRequest.getId());

        watchlistService.postWatchlist(user, media);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteWatchlist(@RequestBody WatchlistRequest watchlistRequest, Authentication authentication) {
        User user = userService.getUser(authentication.getName());
        Media media = mediaService.getMedia(watchlistRequest.getId());

        watchlistService.deleteWatchlist(user, media);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
