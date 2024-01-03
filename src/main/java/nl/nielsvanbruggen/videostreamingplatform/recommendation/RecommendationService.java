package nl.nielsvanbruggen.videostreamingplatform.recommendation;

import com.sun.jdi.InternalException;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTOGeneral;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTOGeneralMapper;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserRepository;
import nl.nielsvanbruggen.videostreamingplatform.watched.WatchedRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final UserRepository userRepository;
    private final WatchedRepository watchedRepository;
    private final RecommendationCache recommendationCache;
    private final MediaDTOGeneralMapper mediaDTOGeneralMapper;
    private final static int MIN_WATCHED_THRESHOLD = 10;
    private final static int MAX_RETURN_ENTRIES = 50;


    public List<MediaDTOGeneral> getRecommendations(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new InternalException("User does not exist."));
        List<Media> watched = watchedRepository.findAllMediaByUser(user);

        if(watched.size() < MIN_WATCHED_THRESHOLD) return List.of();

        return recommendationCache.getUserRecommendations(user).stream()
                .limit(MAX_RETURN_ENTRIES)
                .map(mediaDTOGeneralMapper)
                .toList();
    }
}
