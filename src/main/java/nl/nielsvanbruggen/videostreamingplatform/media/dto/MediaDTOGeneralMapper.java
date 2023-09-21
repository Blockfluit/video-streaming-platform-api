package nl.nielsvanbruggen.videostreamingplatform.media.dto;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.actor.dto.ActorDTOMapper;
import nl.nielsvanbruggen.videostreamingplatform.actor.model.MediaActor;
import nl.nielsvanbruggen.videostreamingplatform.actor.repository.MediaActorRepository;
import nl.nielsvanbruggen.videostreamingplatform.genre.MediaGenreRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Rating;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.RatingRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.ReviewRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.VideoRepository;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MediaDTOGeneralMapper implements Function<Media, MediaDTOGeneral> {
    private final MediaGenreRepository mediaGenreRepository;
    private final MediaActorRepository mediaActorRepository;
    private final VideoRepository videoRepository;
    private final RatingRepository ratingRepository;
    private final ActorDTOMapper actorDTOMapper;

    @Override
    public MediaDTOGeneral apply(Media media) {
        return MediaDTOGeneral.builder()
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
                .videos(videoRepository.findAllByMedia(media).size())
                .rating(ratingRepository.findAllByMedia(media).stream()
                        .mapToDouble(Rating::getScore)
                        .average()
                        .orElse(-1))
                .build();
    }
}
