package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.media.model.IdIndex;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Type;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class MediaPatchRequest {
    private String trailer;
    private MultipartFile thumbnail;
    private List<String> genres;
    private List<Long> actors;
    private Integer year;
    private Type type;
    private String plot;
    private List<IdIndex> order;
    private boolean updateFiles;
}
