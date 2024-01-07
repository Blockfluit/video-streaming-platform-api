package nl.nielsvanbruggen.videostreamingplatform.recommendation;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.actor.model.Actor;
import nl.nielsvanbruggen.videostreamingplatform.actor.model.MediaActor;
import nl.nielsvanbruggen.videostreamingplatform.actor.repository.MediaActorRepository;
import nl.nielsvanbruggen.videostreamingplatform.genre.Genre;
import nl.nielsvanbruggen.videostreamingplatform.genre.MediaGenre;
import nl.nielsvanbruggen.videostreamingplatform.genre.MediaGenreRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTO;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTOSimplifiedMapper;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.MediaRepository;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserRepository;
import nl.nielsvanbruggen.videostreamingplatform.watched.WatchedRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.annotation.EnableCaching;
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
    private final MediaGenreRepository mediaGenreRepository;
    private final MediaActorRepository mediaActorRepository;
    private final MediaDTOSimplifiedMapper mediaDTOSimplifiedMapper;
    private final static Map<Long, List<Genre>> mediaGenreMap = new ConcurrentHashMap<>();
    private final static Map<Long, List<Actor>> mediaActorMap = new ConcurrentHashMap<>();
    private final static Map<String, List<MediaDTO>> recommendations = new ConcurrentHashMap<>();
    private final static int MIN_WATCHED_THRESHOLD = 10;
    private final static int MAX_RETURN_ENTRIES = 50;

    public List<MediaDTO> getRecommendations(Authentication authentication) {
        return recommendations.get(authentication.getName());
    }

    @PostConstruct
    public void initRecommendations() {
        updateMediaActorMap();
        updateMediaGenreMap();
        updateRecommendations();
    }

    @Scheduled(cron = "0 20 0 * * *")
    public void updateRecommendations() {
        userRepository.findAll()
                .forEach(user -> recommendations.put(user.getUsername(), createRecommendations(user)));
    }

    @Scheduled(cron = "0 10 0 * * *")
    public void updateMediaGenreMap() {
        List<MediaGenre> mediaGenreList = mediaGenreRepository.findAll();
        mediaGenreList.stream()
                .map(MediaGenre::getMedia)
                .forEach(media -> mediaGenreMap.put(media.getId(), mediaGenreList.stream()
                        .filter(mediaGenre -> mediaGenre.getMedia().getId() == media.getId())
                        .map(MediaGenre::getGenre)
                        .toList()));
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateMediaActorMap() {
        List<MediaActor> mediaActorList = mediaActorRepository.findAll();
        mediaActorList.stream()
                .map(MediaActor::getMedia)
                .forEach(media -> mediaActorMap.put(media.getId(), mediaActorList.stream()
                        .filter(mediaActor -> mediaActor.getMedia().getId() == media.getId())
                        .map(MediaActor::getActor)
                        .toList()));

    }

    private List<MediaDTO> createRecommendations(User user) {
        List<Media> watched = watchedRepository.findAllMediaByUser(user);

        if (watched.size() < MIN_WATCHED_THRESHOLD) return List.of();

        Map<Genre, Integer> genres = new HashMap<>();
        Map<Actor, Integer> actors = new HashMap<>();
        List<Media> notWatched = mediaRepository.findAll().stream()
                .filter(media -> !watched.contains(media))
                .toList();

        List<Media> mostRecentWatched = watched.stream()
                .limit(MIN_WATCHED_THRESHOLD)
                .toList();

        mostRecentWatched
                .forEach(media -> mediaGenreRepository.findAllByMedia(media)
                        .forEach(mediaGenre -> genres.merge(mediaGenre.getGenre(), 1, Integer::sum))
                );
        mostRecentWatched
                .forEach(media -> mediaActorRepository.findAllByMedia(media)
                        .forEach(mediaActor -> actors.merge(mediaActor.getActor(), 1, Integer::sum))
                );

        List<Media> recommendations = notWatched.stream()
                .sorted(Comparator
                        .comparingDouble((object) ->
                                getGenreSimilarityScore((Media) object, genres) +
                                        getActorSimilarityScore((Media) object, actors))
                        .reversed())
                .toList();

        return recommendations.stream()
                .limit(MAX_RETURN_ENTRIES)
                .map(mediaDTOSimplifiedMapper)
                .toList();
    }

    private double getGenreSimilarityScore(Media media, Map<Genre, Integer> genres) {
        if(mediaGenreMap.get(media.getId()) == null) return 0;

        return mediaGenreMap.get(media.getId()).stream()
                .mapToInt(genre -> {
                    Integer score = genres.get(genre);
                    return score == null ? 0 : score;
                })
                .sum();
    }

    private double getActorSimilarityScore(Media media, Map<Actor, Integer> actors) {
        if(mediaActorMap.get(media.getId()) == null) return 0;

        return mediaActorMap.get(media.getId()).stream()
                .mapToInt(actor -> {
                    Integer score = actors.get(actor);
                    return score == null ? 0 : score;
                })
                .sum();
    }
}
