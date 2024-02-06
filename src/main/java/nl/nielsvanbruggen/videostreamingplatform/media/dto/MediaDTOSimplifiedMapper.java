package nl.nielsvanbruggen.videostreamingplatform.media.dto;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.genre.Genre;
import nl.nielsvanbruggen.videostreamingplatform.genre.MediaGenre;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Rating;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.RatingRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.ReviewRepository;
import nl.nielsvanbruggen.videostreamingplatform.video.dto.VideoDTOMapper;
import nl.nielsvanbruggen.videostreamingplatform.watched.repository.WatchedRepository;
import nl.nielsvanbruggen.videostreamingplatform.actor.dto.ActorDTOMapper;
import nl.nielsvanbruggen.videostreamingplatform.actor.model.MediaActor;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.video.repository.VideoRepository;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MediaDTOSimplifiedMapper implements Function<Media, MediaDTO> {
    private final RatingRepository ratingRepository;
    private final RatingDTOMapper ratingDTOMapper;
    private final ReviewRepository reviewRepository;
    private final ReviewDTOMapper reviewDTOMapper;
    private final VideoRepository videoRepository;
    private final VideoDTOMapper videoDTOMapper;
    private final WatchedRepository watchedRepository;

    @Override
    public MediaDTO apply(Media media) {
        return MediaDTO.builder()
                .id(media.getId())
                .name(media.getName())
                .thumbnail(media.getThumbnail())
                .trailer(media.getTrailer())
                .plot(media.getPlot())
                .type(media.getType())
                .year(media.getYear())
                .updatedAt(media.getUpdatedAt())
                .createdAt(media.getCreatedAt())
                .genres(media.getGenres().stream()
                        .map(MediaGenre::getGenre)
                        .map(Genre::getName)
                        .toList())
                .actors(media.getActors().stream()
                        .map(MediaActor::getActor)
                        .toList())
                .videoCount(videoRepository.countByMedia(media))
                .videos(videoRepository.findFirstByMedia(media).stream()
                        .map(videoDTOMapper)
                        .toList())
                .ratings(ratingRepository.findAllByMedia(media).stream()
                        .map(ratingDTOMapper)
                        .toList())
                .reviews(reviewRepository.findAllByMedia(media).stream()
                        .map(reviewDTOMapper)
                        .toList())
                .views(watchedRepository.totalUniqueViewsByMedia(media))
                .avgRating(ratingRepository.averageScoreByMedia(media)
                        .orElse(-1D))
                .build();
    }
}
