package nl.nielsvanbruggen.videostreamingplatform.media.dto;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.genre.MediaGenreRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.RatingRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.ReviewRepository;
import nl.nielsvanbruggen.videostreamingplatform.person.model.ContextRole;
import nl.nielsvanbruggen.videostreamingplatform.person.model.MediaPerson;
import nl.nielsvanbruggen.videostreamingplatform.genre.Genre;
import nl.nielsvanbruggen.videostreamingplatform.genre.MediaGenre;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Rating;
import nl.nielsvanbruggen.videostreamingplatform.person.repository.MediaPersonRepository;
import nl.nielsvanbruggen.videostreamingplatform.video.dto.VideoDTOMapper;
import nl.nielsvanbruggen.videostreamingplatform.video.repository.VideoRepository;
import nl.nielsvanbruggen.videostreamingplatform.watched.repository.WatchedRepository;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MediaDTOMapper implements Function<Media, MediaDTO> {
    private final RatingRepository ratingRepository;
    private final RatingDTOMapper ratingDTOMapper;
    private final ReviewDTOMapper reviewDTOMapper;
    private final ReviewRepository reviewRepository;
    private final VideoDTOMapper videoDTOMapper;
    private final VideoRepository videoRepository;
    private final WatchedRepository watchedRepository;
    private final MediaGenreRepository mediaGenreRepository;
    private final MediaPersonRepository mediaPersonRepository;

    @Override
    public MediaDTO apply(Media media) {
        return MediaDTO.builder()
                .id(media.getId())
                .imdbId(media.getImdbId())
                .imdbRating(media.getImdbRating())
                .imdbRatingsAmount(media.getImdbRatingsAmount())
                .name(media.getName())
                .thumbnail(media.getThumbnail())
                .trailer(media.getTrailer())
                .plot(media.getPlot())
                .type(media.getType())
                .year(media.getYear())
                .hidden(media.isHidden())
                .updatedAt(media.getUpdatedAt())
                .createdAt(media.getCreatedAt())
                .genres(mediaGenreRepository.findAllByMedia(media).stream()
                .map(MediaGenre::getGenre)
                .map(Genre::getName)
                .toList())
                .directors(mediaPersonRepository.findAllByMediaAndContextRole(media, ContextRole.DIRECTOR).stream()
                        .map(MediaPerson::getPerson)
                        .toList())
                .creators(mediaPersonRepository.findAllByMediaAndContextRole(media, ContextRole.CREATOR).stream()
                        .map(MediaPerson::getPerson)
                        .toList())
                .writers(mediaPersonRepository.findAllByMediaAndContextRole(media, ContextRole.WRITER).stream()
                        .map(MediaPerson::getPerson)
                        .toList())
                .stars(mediaPersonRepository.findAllByMediaAndContextRole(media, ContextRole.STAR).stream()
                        .map(MediaPerson::getPerson)
                        .toList())
                .cast(mediaPersonRepository.findAllByMediaAndContextRole(media, ContextRole.CAST).stream()
                        .map(MediaPerson::getPerson)
                        .toList())
                .videoCount(videoRepository.countByMedia(media))
                .videos(videoRepository.findAllByMedia(media).stream()
                        .map(videoDTOMapper)
                        .collect(Collectors.toList()))
                .ratings(ratingRepository.findAllByMedia(media).stream()
                        .map(ratingDTOMapper)
                        .collect(Collectors.toList()))
                .reviews(reviewRepository.findAllByMedia(media).stream()
                        .map(reviewDTOMapper)
                        .collect(Collectors.toList()))
                .views(watchedRepository.totalUniqueViewsByMedia(media))
                .avgRating(ratingRepository.findAllByMedia(media).stream()
                        .mapToDouble(Rating::getScore)
                        .average()
                        .orElse(-1D))
                .build();
    }
}
