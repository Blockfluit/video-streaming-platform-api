package nl.nielsvanbruggen.videostreamingplatform.recommendation;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.actor.model.Actor;
import nl.nielsvanbruggen.videostreamingplatform.genre.Genre;
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
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@EnableCaching
@EnableScheduling
public class RecommendationService {
    private final static int MIN_WATCHED_THRESHOLD = 10;
    private final static int MAX_RETURN_ENTRIES = 50;
    private final static Map<String, Recommendation> recommendations = new ConcurrentHashMap<>();
    private final WatchedRepository watchedRepository;
    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;
    private final MediaDTOSimplifiedMapper mediaDTOSimplifiedMapper;

    public Recommendation getRecommendations(User user) {
        return recommendations.get(user.getUsername());
    }

    @PostConstruct
    public void initRecommendations() {
        updateRecommendations();
    }

    @Scheduled(cron = "0 0 0/1 1/1 * *")
    public void updateRecommendations() {
        for(User user: userRepository.findAll()) {
            Recommendation recommendation = Recommendation.builder()
                    .content(List.of())
                    .input(List.of())
                    .threshold(MIN_WATCHED_THRESHOLD)
                    .build();
            List<Media> watched = watchedRepository.findAllMediaByUser(user);

            if (watched.size() > MIN_WATCHED_THRESHOLD) {
                List<Media> notWatched = mediaRepository.findAllNotWatchedByUser(user);
                List<Media> recent = mediaRepository.findAllRecentWatchedByUserAndType(user, "", Pageable.ofSize(MIN_WATCHED_THRESHOLD))
                        .getContent();
                recommendation = createRecommendations(notWatched, recent);
            }
            recommendations.put(user.getUsername(), recommendation);
        }
    }

    private Recommendation createRecommendations(List<Media> notWatched, List<Media> recent) {
        Comparator<Object> compareGenresAndActors = Comparator
                .comparingInt(media ->
                        getGenreSimilarityScore((Media) media, createGenreMap(recent)) +
                        getActorSimilarityScore((Media) media, createActorMap(recent)))
                .reversed();

        return Recommendation.builder()
                .content(notWatched.stream()
                        .sorted(compareGenresAndActors)
                        .limit(MAX_RETURN_ENTRIES)
                        .map(mediaDTOSimplifiedMapper)
                        .toList())
                .input(recent.stream()
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
