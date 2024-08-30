package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Type;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class MediaPostRequest {
    @NotBlank(message = "Name missing.")
    private String name;
    private String trailer;
    private MultipartFile thumbnail;
    private List<String> genres;
    private List<Long> actors;
    @NotNull
    private Integer year;
    private Type type;
    @NotBlank(message = "Plot missing.")
    private String plot;
    private boolean hidden;
}
