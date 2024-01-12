package nl.nielsvanbruggen.videostreamingplatform.watched.service;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Video;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.VideoRepository;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserRepository;
import nl.nielsvanbruggen.videostreamingplatform.watched.model.Watched;
import nl.nielsvanbruggen.videostreamingplatform.watched.dto.WatchedDTO;
import nl.nielsvanbruggen.videostreamingplatform.watched.dto.WatchedDTOMapper;
import nl.nielsvanbruggen.videostreamingplatform.watched.repository.WatchedRepository;
import nl.nielsvanbruggen.videostreamingplatform.watched.controller.WatchedPostRequest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableCaching
public class WatchedService {
    private final UserRepository userRepository;
    private final WatchedRepository watchedRepository;
    private final VideoRepository videoRepository;
    private final WatchedDTOMapper watchedDTOMapper;

    @Cacheable(value = "allWatched", key = "#authentication.getName()")
    public List<WatchedDTO> getAllWatched(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist."));

        return watchedRepository.findAllByUser(user).stream()
                .map(watchedDTOMapper)
                .toList();
    }


    @CacheEvict(value = "allWatched", key = "#authentication.getName()")
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
    }
}
