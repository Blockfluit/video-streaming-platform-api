package nl.nielsvanbruggen.videostreamingplatform.video.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class VideoDTO {
        private long id;
        private String name;
        private double duration;
        private int index;
        private int season;
        private List<SubtitleDTO> subtitles;
        private Integer xResolution;
        private Integer yResolution;
}
