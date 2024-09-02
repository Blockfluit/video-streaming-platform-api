package nl.nielsvanbruggen.videostreamingplatform.video.dto;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import nl.nielsvanbruggen.videostreamingplatform.video.repository.SubtitleRepository;
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
        return VideoDTO.builder()
                .id(video.getId())
                .name(video.getName())
                .duration(video.getDuration())
                .index(video.getIndex())
                .season(video.getSeason())
                .subtitles(subtitleRepository.findAllByVideo(video).stream()
                        .map(subtitleDTOMapper)
                        .collect(Collectors.toList()))
                .xResolution(video.getXResolution())
                .yResolution(video.getYResolution())
                .build();
    }
}
