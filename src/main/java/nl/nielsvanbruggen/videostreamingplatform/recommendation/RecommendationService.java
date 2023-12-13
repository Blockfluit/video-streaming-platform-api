package nl.nielsvanbruggen.videostreamingplatform.recommendation;

import com.sun.jdi.InternalException;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.actor.model.Actor;
import nl.nielsvanbruggen.videostreamingplatform.actor.repository.MediaActorRepository;
import nl.nielsvanbruggen.videostreamingplatform.genre.Genre;
import nl.nielsvanbruggen.videostreamingplatform.genre.MediaGenreRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.MediaRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.RatingRepository;
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
    private final MediaRepository mediaRepository;
    private final WatchedRepository watchedRepository;
    private final RatingRepository ratingRepository;
    private final MediaGenreRepository mediaGenreRepository;
    private final MediaActorRepository mediaActorRepository;
    private final static int MIN_WATCHED_THRESHOLD = 10;
    private final static int MAX_RETURN_ENTRIES = 20;

    public List<?> getRecommendations(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new InternalException("User does not exist."));
        List<Media> watched = watchedRepository.findAllMediaByUser(user);

        if(watched.size() < MIN_WATCHED_THRESHOLD) return List.of();

        List<Pair<Genre, Integer>> genres = new ArrayList<>();
        List<Pair<Actor, Integer>> actors = new ArrayList<>();

        watched.forEach(media -> {
            mediaGenreRepository.findAllByMedia(media)
                    .forEach(mediaGenre -> genres.stream()
                            .filter(pair -> pair.key.equals(mediaGenre.getGenre()))
                            .findFirst()
                            .ifPresentOrElse(
                                    (pair) -> pair.setValue(pair.getValue() + 1),
                                    () -> genres.add(new Pair<>(mediaGenre.getGenre(), 1))
                            )
                    );
            mediaActorRepository.findAllByMedia(media)
                    .forEach(mediaActor -> actors.stream()
                            .filter(pair -> pair.key.equals(mediaActor.getActor()))
                            .findFirst()
                            .ifPresentOrElse(
                                    (pair) -> pair.setValue(pair.getValue() + 1),
                                    () -> actors.add(new Pair<>(mediaActor.getActor(), 1))
                            )
                    );
        });

        Comparator<Media> compareByViewsAndRatings = Comparator
                .comparing(watchedRepository::totalUniqueViewsByMedia)
                .thenComparingDouble(media -> ratingRepository.averageScoreByMedia(media)
                        .orElse(-1D)
                )
                .reversed();

        return mediaRepository.findAll().stream()
                .filter(media -> !watched.contains(media))
                .sorted(Comparator
                        .comparing(watchedRepository::totalUniqueViewsByMedia)
                        .thenComparingDouble(media -> ratingRepository.averageScoreByMedia(media)
                                .orElse(-1D)
                        )
                        .reversed())
                .toList();
    }
}
