package nl.nielsvanbruggen.videostreamingplatform.video.dto;

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
