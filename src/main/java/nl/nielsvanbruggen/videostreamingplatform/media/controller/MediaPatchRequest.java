package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.media.model.IdIndex;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Type;

import java.util.List;

@Data
public class MediaPatchRequest {
    private String trailer;
    private String thumbnail;
    private List<String> genres;
    private List<Long> actors;
    private Integer year;
    private Type type;
    private String plot;
    private List<IdIndex> order;
    private boolean updateFiles;
    private boolean hidden;
    private boolean updateTimestamp;
}
