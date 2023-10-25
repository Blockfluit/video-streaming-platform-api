package nl.nielsvanbruggen.videostreamingplatform.media.dto;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Video;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.SubtitleRepository;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VideoDTOMapper implements Function<Video, VideoDTO> {
    private final SubtitleRepository subtitleRepository;
    private final SubtitleDTOMapper subtitleDTOMapper;

    @Override
    public VideoDTO apply(Video video) {
        return new VideoDTO(
                video.getId(),
                video.getName(),
                video.getDuration(),
                video.getIndex(),
                video.getSeason(),
                subtitleRepository.findAllByVideo(video).stream()
                        .map(subtitleDTOMapper)
                        .collect(Collectors.toList())
        );
    }
}
