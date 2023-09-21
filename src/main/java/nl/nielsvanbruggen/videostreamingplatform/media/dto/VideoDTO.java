package nl.nielsvanbruggen.videostreamingplatform.media.dto;

import nl.nielsvanbruggen.videostreamingplatform.media.model.Subtitle;

import java.util.List;
public record VideoDTO(
        long id,
        String name,
        double duration,
        int index,
        List<SubtitleDTO> subtitles
) {

}
