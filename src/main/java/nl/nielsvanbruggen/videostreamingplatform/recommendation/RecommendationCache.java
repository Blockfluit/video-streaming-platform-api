package nl.nielsvanbruggen.videostreamingplatform.recommendation;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.actor.model.Actor;
import nl.nielsvanbruggen.videostreamingplatform.actor.repository.MediaActorRepository;
import nl.nielsvanbruggen.videostreamingplatform.genre.Genre;
import nl.nielsvanbruggen.videostreamingplatform.genre.MediaGenreRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.MediaRepository;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.watched.WatchedRepository;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecommendationCache {
    private final MediaRepository mediaRepository;
    private final WatchedRepository watchedRepository;
    private final MediaGenreRepository mediaGenreRepository;
    private final MediaActorRepository mediaActorRepository;
    private final static MultiValuedMap<Long, Media> userRecommendations = new ArrayListValuedHashMap<>();
    private final static MultiValuedMap<Media, Genre> allGenres = new ArrayListValuedHashMap<>();
    private final static MultiValuedMap<Media, Actor> allActors = new ArrayListValuedHashMap<>();
    public final static Map<Long, Boolean> userRevalidate = new HashMap<>();
    public static boolean allGenresRevalidate = true;
    public static boolean allActorsRevalidate = true;

    public List<Media> getUserRecommendations(User user) {
        Boolean revalidate = userRevalidate.get(user.getId());
        if(revalidate == null || revalidate) {
            updateUserRecommendation(user);
            userRevalidate.put(user.getId(), false);
        }

        return (List<Media>) userRecommendations.get(user.getId());
    }

    private void updateUserRecommendation(User user) {
        Map<Genre, Integer> genres = new HashMap<>();
        Map<Actor, Integer> actors = new HashMap<>();
        List<Media> watched = watchedRepository.findAllMediaByUser(user);
        List<Media> notWatched = mediaRepository.findAll().stream()
                .filter(media -> !watched.contains(media))
                .toList();

        watched.forEach(media -> mediaGenreRepository.findAllByMedia(media)
                .forEach(mediaGenre -> genres.merge(mediaGenre.getGenre(), 1, Integer::sum))
        );
        watched.forEach(media -> mediaActorRepository.findAllByMedia(media)
                .forEach(mediaActor -> actors.merge(mediaActor.getActor(), 1, Integer::sum))
        );

        Comparator<Object> compareByGenresAndActors = Comparator
                .comparingDouble((object) ->
                        getGenreSimilarityScore((Media) object, genres) +
                        getActorSimilarityScore((Media) object, actors))
                .reversed();

        List<Media> recommendations = notWatched.stream()
                .sorted(compareByGenresAndActors)
                .toList();

        userRecommendations.putAll(user.getId(), recommendations);
    }

    private MultiValuedMap<Media, Genre> getAllGenres() {
        if(allGenresRevalidate) {
            updateAllGenres();
            allGenresRevalidate = false;
        }

        return allGenres;
    }

    private MultiValuedMap<Media, Actor> getAllActors() {
        if(allActorsRevalidate) {
            updateAllActors();
            allActorsRevalidate = false;
        }

        return allActors;
    }

    private double getGenreSimilarityScore(Media media, Map<Genre, Integer> genres) {
        return getAllGenres()
                .get(media).stream()
                .mapToInt(genre -> {
                    Integer score = genres.get(genre);
                    return score == null ? 0 : score;})
                .sum();
    }

    private double getActorSimilarityScore(Media media, Map<Actor, Integer> actors) {
       return getAllActors()
                .get(media).stream()
                .mapToInt(actor -> {
                    Integer score = actors.get(actor);
                    return score == null ? 0 : score;})
                .sum();
    }

    private synchronized void updateAllGenres() {
        allGenres.clear();
        mediaGenreRepository.findAll()
                .forEach(mediaGenre -> allGenres.put(mediaGenre.getMedia(), mediaGenre.getGenre()));
    }

    private synchronized void updateAllActors() {
        allActors.clear();
        mediaActorRepository.findAll()
                .forEach(mediaActor -> allActors.put(mediaActor.getMedia(), mediaActor.getActor()));
    }
}
