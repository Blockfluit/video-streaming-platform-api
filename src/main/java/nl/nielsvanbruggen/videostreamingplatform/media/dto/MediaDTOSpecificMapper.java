package nl.nielsvanbruggen.videostreamingplatform.media.dto;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.watched.WatchedRepository;
import nl.nielsvanbruggen.videostreamingplatform.actor.dto.ActorDTOMapper;
import nl.nielsvanbruggen.videostreamingplatform.actor.model.MediaActor;
import nl.nielsvanbruggen.videostreamingplatform.actor.repository.MediaActorRepository;
import nl.nielsvanbruggen.videostreamingplatform.genre.MediaGenreRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.RatingRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.ReviewRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.VideoRepository;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MediaDTOSpecificMapper implements Function<Media, MediaDTOSpecific> {
    private final MediaGenreRepository mediaGenreRepository;
    private final MediaActorRepository mediaActorRepository;
    private final RatingRepository ratingRepository;
    private final VideoRepository videoRepository;
    private final ActorDTOMapper actorDTOMapper;
    private final VideoDTOMapper videoDTOMapper;
    private final RatingDTOMapper ratingDTOMapper;
    private final ReviewDTOMapper reviewDTOMapper;
    private final ReviewRepository reviewRepository;
    private final WatchedRepository watchedRepository;

    @Override
    public MediaDTOSpecific apply(Media media) {
        return MediaDTOSpecific.builder()
                .id(media.getId())
                .name(media.getName())
                .thumbnail(media.getThumbnail())
                .trailer(media.getTrailer())
                .plot(media.getPlot())
                .type(media.getType())
                .year(media.getYear())
                .updatedAt(media.getUpdatedAt())
                .createdAt(media.getCreatedAt())
                .genre(mediaGenreRepository.findAllByMedia(media).stream()
                        .map(mediaGenre -> mediaGenre.getGenre().name)
                        .collect(Collectors.toList()))
                .actors(mediaActorRepository.findAllByMedia(media).stream()
                        .map(MediaActor::getActor)
                        .map(actorDTOMapper)
                        .collect(Collectors.toList()))
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
                .build();
    }
}
