package nl.nielsvanbruggen.videostreamingplatform.watched;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Video;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.VideoRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.service.MediaService;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WatchedService {
    private final UserRepository userRepository;
    private final WatchedRepository watchedRepository;
    private final VideoRepository videoRepository;
    private final WatchedDTOMapper watchedDTOMapper;
    private final MediaService mediaService;

    public List<WatchedDTO> getWatched(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist."));

        return watchedRepository.findAllByUser(user).stream()
                .map(watchedDTOMapper)
                .collect(Collectors.toList());
    }

    public void postWatched(WatchedPostRequest request, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist."));
        Video video = videoRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Video does not exist."));

        Watched watched = watchedRepository.findByUserAndVideo(user, video)
                .orElseGet(() -> Watched.builder()
                        .user(user)
                        .video(video)
                        .createdAt(Instant.now())
                        .build());
        watched.setUpdatedAt(Instant.now());
        watched.setTimestamp(request.getTimestamp());

        watchedRepository.save(watched);
        mediaService.updateAllMedia();
    }
}
