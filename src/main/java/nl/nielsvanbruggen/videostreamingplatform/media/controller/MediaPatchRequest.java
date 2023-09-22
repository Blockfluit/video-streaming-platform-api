package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import lombok.Data;
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
    private boolean updateFiles;
}
