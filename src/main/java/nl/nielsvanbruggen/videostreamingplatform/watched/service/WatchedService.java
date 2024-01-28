package nl.nielsvanbruggen.videostreamingplatform.watched.service;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.watched.model.Watched;
import nl.nielsvanbruggen.videostreamingplatform.watched.dto.WatchedDTO;
import nl.nielsvanbruggen.videostreamingplatform.watched.dto.WatchedDTOMapper;
import nl.nielsvanbruggen.videostreamingplatform.watched.repository.WatchedRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableCaching
public class WatchedService {
    private final WatchedRepository watchedRepository;
    private final WatchedDTOMapper watchedDTOMapper;

    @Cacheable(value = "allWatched", key = "#user.getId()")
    public List<WatchedDTO> getAllWatched(User user) {
        return watchedRepository.findAllByUser(user).stream()
                .map(watchedDTOMapper)
                .toList();
    }

    @CacheEvict(value = "allWatched", key = "#user.getId()")
    public void postWatched(User user, Video video, float timestamp) {
        Watched watched = watchedRepository.findByUserAndVideo(user, video)
                .orElseGet(() -> Watched.builder()
                        .user(user)
                        .video(video)
                        .createdAt(Instant.now())
                        .build());
        watched.setUpdatedAt(Instant.now());
        watched.setTimestamp(timestamp);

        watchedRepository.save(watched);
    }
}
