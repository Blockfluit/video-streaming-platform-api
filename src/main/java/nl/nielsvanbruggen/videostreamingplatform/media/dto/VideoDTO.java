package nl.nielsvanbruggen.videostreamingplatform.media.dto;

import java.util.List;
public record VideoDTO(
        long id,
        String name,
        double duration,
        int index,
        int season,
        List<SubtitleDTO> subtitles
) {

}
