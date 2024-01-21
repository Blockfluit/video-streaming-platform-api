package nl.nielsvanbruggen.videostreamingplatform.recommendation;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.actor.model.Actor;
import nl.nielsvanbruggen.videostreamingplatform.genre.Genre;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTO;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTOSimplifiedMapper;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.MediaRepository;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserRepository;
import nl.nielsvanbruggen.videostreamingplatform.watched.repository.WatchedRepository;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@EnableCaching
@EnableScheduling
// TODO: Algorithms should be faster if possible.
public class RecommendationService {
    private final UserRepository userRepository;
    private final WatchedRepository watchedRepository;
    private final MediaRepository mediaRepository;
    private final MediaDTOSimplifiedMapper mediaDTOSimplifiedMapper;
    private final static Map<String, Recommendation> recommendations = new ConcurrentHashMap<>();
    private final static int MIN_WATCHED_THRESHOLD = 10;
    private final static int MAX_RETURN_ENTRIES = 50;

    public Recommendation getRecommendations(Authentication authentication) {
        return recommendations.get(authentication.getName());
    }

    @PostConstruct
    public void initRecommendations() {
        updateRecommendations();
    }

    @Scheduled(cron = "0 0 0/1 1/1 * *")
    public void updateRecommendations() {
        userRepository.findAll()
                .forEach(user -> recommendations.put(user.getUsername(), createRecommendations(user)));
    }

    private Recommendation createRecommendations(User user) {
        List<Media> watched = watchedRepository.findAllMediaByUser(user);

        if (watched.size() < MIN_WATCHED_THRESHOLD) {
            return Recommendation.builder()
                    .content(List.of())
                    .input(List.of())
                    .build();
        }

        List<Media> notWatched = mediaRepository.findAll().stream()
                .filter(media -> !watched.contains(media))
                .toList();

        List<Media> recentMedia = mediaRepository.findAllRecentWatched(user, "", Pageable.ofSize(MIN_WATCHED_THRESHOLD))
                .getContent();

        return Recommendation.builder()
                .content(notWatched.stream()
                        .sorted(Comparator
                                .comparingInt(media -> getGenreSimilarityScore((Media) media, createGenreMap(recentMedia)) +
                                        getActorSimilarityScore((Media) media, createActorMap(recentMedia)))
                                .reversed())
                        .limit(MAX_RETURN_ENTRIES)
                        .map(mediaDTOSimplifiedMapper)
                        .toList())
                .input(recentMedia.stream()
                        .map(mediaDTOSimplifiedMapper)
                        .toList())
                .threshold(MIN_WATCHED_THRESHOLD)
                .build();
    }

    private Map<Genre, Integer> createGenreMap(List<Media> recentMedia) {
        Map<Genre, Integer> genreMap = new HashMap<>();
        recentMedia.forEach(media -> media.getGenres()
                .forEach(mediaGenre -> genreMap.merge(mediaGenre.getGenre(), 1, Integer::sum)));
        return genreMap;
    }

    private Map<Actor, Integer> createActorMap(List<Media> recentMedia) {
        Map<Actor, Integer> actorMap = new HashMap<>();
        recentMedia.forEach(media -> media.getActors()
                .forEach(mediaActor -> actorMap.merge(mediaActor.getActor(), 1, Integer::sum)));
        return actorMap;
    }

    private int getGenreSimilarityScore(Media media, Map<Genre, Integer> genres) {
        return media.getGenres().stream()
                .mapToInt(mediaGenre -> {
                    Integer score = genres.get(mediaGenre.getGenre());
                    return score == null ? 0 : score;
                })
                .sum();
    }

    private int getActorSimilarityScore(Media media, Map<Actor, Integer> actors) {
        return media.getActors().stream()
                .mapToInt(mediaActor -> {
                    Integer score = actors.get(mediaActor.getActor());
                    return score == null ? 0 : score;
                })
                .sum();
    }
}
