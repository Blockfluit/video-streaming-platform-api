package nl.nielsvanbruggen.videostreamingplatform.watchlist;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTO;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTOMapper;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.MediaRepository;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WatchlistService {
    private final UserRepository userRepository;
    private final WatchlistRepository watchlistRepository;
    private final MediaRepository mediaRepository;
    private final MediaDTOMapper mediaDTOMapper;


    public List<MediaDTO> getWatchlist(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist."));

        return watchlistRepository.findAllByUser(user).stream()
                .map(Watchlist::getMedia)
                .map(mediaDTOMapper)
                .collect(Collectors.toList());
    }

    public void postWatchlist(WatchlistRequest request, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist."));
        Media media = mediaRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Media does not exist."));
        Watchlist watchlist = Watchlist.builder()
                .user(user)
                .media(media)
                .createdAt(Instant.now())
                .build();

        watchlistRepository.save(watchlist);
    }

    public void deleteWatchlist(WatchlistRequest request, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist."));
        Media media = mediaRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Media does not exist."));
        Watchlist watchlist = watchlistRepository.findById(new WatchlistId(media, user))
                .orElseThrow(() -> new IllegalArgumentException("Watchlist entry does not exist."));

        watchlistRepository.delete(watchlist);
    }
}
