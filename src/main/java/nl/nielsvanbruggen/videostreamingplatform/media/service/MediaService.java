package nl.nielsvanbruggen.videostreamingplatform.media.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.nielsvanbruggen.videostreamingplatform.genre.Genre;
import nl.nielsvanbruggen.videostreamingplatform.genre.GenreRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.controller.*;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTO;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.MediaDTOSimplifiedMapper;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Rating;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Review;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.MediaRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.RatingRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.ReviewRepository;
import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.service.UserService;
import nl.nielsvanbruggen.videostreamingplatform.watched.repository.WatchedRepository;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableCaching
@Slf4j
public class MediaService {
    private final MediaRepository mediaRepository;
    private final GenreRepository genreRepository;
    private final RatingRepository ratingRepository;
    private final ReviewRepository reviewRepository;
    private final WatchedRepository watchedRepository;
    private final MediaDTOSimplifiedMapper mediaDTOSimplifiedMapper;
    private final UserService userService;
    private final UploadService uploadService;
    private final UpdateService updateService;

    public Media getMedia(long id) {
        return mediaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id does not exist."));
    }

    public Media save(Media media) {
        return mediaRepository.save(media);
    }

    public Page<MediaDTO> getMedia(int pageNumber, int pageSize, String type, List<String> genres, String search, boolean overrideHidden) {
        List<Genre> tmpGenres = genres.isEmpty() ?
                genreRepository.findAll() :
                genres.stream()
                        .map(Genre::new)
                        .toList();

        return mediaRepository.findAllByPartialNameTypeAndGenres(search, type, tmpGenres, overrideHidden, PageRequest.of(pageNumber, pageSize))
                .map(mediaDTOSimplifiedMapper);
    }

    public Page<Media> getAutocompletion(int pageNumber, int pageSize, String type, List<String> genres, String search, boolean overrideHidden) {
        if(search.isEmpty()) return Page.empty(PageRequest.of(pageNumber, pageSize));

        List<Genre> tmpGenres = genres.isEmpty() ?
                genreRepository.findAll() :
                genres.stream()
                        .map(Genre::new)
                        .toList();
        return mediaRepository.findAutoCompletion(search, type, tmpGenres, overrideHidden, PageRequest.of(pageNumber, pageSize));
    }

    public Page<MediaDTO> getRecentUploaded(int pageNumber, int pageSize, String type, boolean overrideHidden) {
        return mediaRepository.findAllRecentUploadedByType(type, Instant.now().minus(7, ChronoUnit.DAYS), overrideHidden, PageRequest.of(pageNumber, pageSize))
                .map(mediaDTOSimplifiedMapper);
    }

    public Page<MediaDTO> getBestRated(int pageNumber, int pageSize, String type, boolean overrideHidden) {
        return mediaRepository.findAllBestRatedByType(type, overrideHidden, PageRequest.of(pageNumber, pageSize))
                .map(mediaDTOSimplifiedMapper);
    }

    public Page<MediaDTO> getMostWatched(int pageNumber, int pageSize, String type, boolean overrideHidden) {
        return mediaRepository.findAllMostWatchedByType(type, overrideHidden, PageRequest.of(pageNumber, pageSize))
                .map(mediaDTOSimplifiedMapper);
    }

    public Page<MediaDTO> getLastWatched(int pageNumber, int pageSize, String type, boolean overrideHidden) {
         return mediaRepository.findAllLastWatchedByType(type, overrideHidden, PageRequest.of(pageNumber, pageSize))
                 .map(mediaDTOSimplifiedMapper);
    }


    public Page<MediaDTO> getRecentWatched(User user, int pageNumber, int pageSize, String type) {
        return watchedRepository.findAllWatchedByUserAndGroupedByMediaId(user, type, PageRequest.of(pageNumber, pageSize))
                .map(mediaDTOSimplifiedMapper);
    }

    public void postRating(Long id, RatingPostRequest request, Authentication authentication) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id does not exist."));

        User user = userService.getUser(authentication.getName());

        Rating rating = Rating.builder()
                .media(media)
                .user(user)
                .score(request.getRating())
                .build();

        ratingRepository.save(rating);
    }

    public void postReview(Long id, ReviewPostRequest request, Authentication authentication) {
        if(!(authentication.getAuthorities().contains(new SimpleGrantedAuthority(Role.CRITIC.name())) ||
                authentication.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name())))) {
            throw new IllegalArgumentException("Insufficient permission");
        }

        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id does not exist."));

        User user = userService.getUser(authentication.getName());

        Review review = Review.builder()
                .media(media)
                .title(request.getTitle())
                .comment(request.getComment())
                .user(user)
                .updatedAt(Instant.now())
                .createdAt(Instant.now())
                .build();

        reviewRepository.save(review);
    }

    public void patchReview(Long id, ReviewPatchRequest request, Authentication authentication) {
        if(!authentication.getAuthorities().contains(new SimpleGrantedAuthority(Role.CRITIC.name())) &&
                !authentication.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()))) {
            throw new IllegalArgumentException("Insufficient permission");
        }

        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id does not exist."));

        User user = userService.getUser(authentication.getName());

        Review review = reviewRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Review does not exist"));

        if(!user.equals(review.getUser())) {
            throw new IllegalArgumentException("Insufficient permission");
        }
        review.setUpdatedAt(Instant.now());
        review.setTitle(request.getTitle());
        review.setComment(request.getComment());

        reviewRepository.save(review);
    }

    public void deleteReview(Long id, ReviewDeleteRequest request, Authentication authentication) {
        User user = userService.getUser(authentication.getName());

        Review review = reviewRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Review does not exist"));

        if(!user.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name())) &&
                !review.getUser().equals(user)) {
            throw new IllegalArgumentException("Insufficient permission");
        }

        reviewRepository.delete(review);
    }

    @Transactional
    public void patchMedia(Long id, MediaPatchRequest request) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id does not exist."));

        if(request.isScrapeImdb() && request.getImdbId() == null && media.getImdbId() == null) {
            throw new IllegalArgumentException("Neither the media nor the request contains an imdb id. So scraping is not possible.");
        }

        if(request.isScrapeImdb()) {
            log.info("Updating existing media: ({}) with data from Imdb...", media.getName());
            updateService.updateImdb(media, request);
        } else {
            log.info("Updating existing media: ({})...", media.getName());
            updateService.updateDefault(media, request);
        }
    }

    @Transactional
    public void postMedia(MediaPostRequest request, Authentication authentication) {
        User user = userService.getUser(authentication.getName());

        if(mediaRepository.findByName(request.getName()).isPresent()) {
            throw new IllegalArgumentException("Media already exists.");
        }

        if(mediaRepository.findByImdbId(request.getImdbId()).isPresent()) {
            throw new IllegalArgumentException("Media with this imdb id already exists.");
        }

        if(request.isScrapeImdb() && request.getImdbId() == null) {
            throw new IllegalArgumentException("Must provide a imdb id in order to scrape.");
        }

        if(request.isScrapeImdb()) {
            log.info("Creating new media: ({}) and aggregating with data from Imdb... ", request.getName());
            uploadService.imdbUpload(request, user);
        } else {
            log.info("Creating new media: ({})...", request.getName());
            uploadService.defaultUpload(request, user);
        }
    }

    @Transactional
    public void deleteMedia(Long id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id does not exist."));

        mediaRepository.delete(media);
    }
}