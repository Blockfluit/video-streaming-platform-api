package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.annotations.IsBase64Image;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Type;

import java.util.Set;

@Data
public class MediaPostRequest {
    @NotBlank(message = "Name missing.")
    private String name;
    private String imdbId;
    @NotBlank(message = "Trailer missing.")
    private String trailer;
    @IsBase64Image
    @NotNull
    private String thumbnail;
    private Set<String> genres = Set.of();
    private Set<Long> directors = Set.of();
    private Set<Long> writers = Set.of();
    private Set<Long> creators = Set.of();
    private Set<Long> stars = Set.of();
    private Set<Long> cast = Set.of();
    private Integer year;
    @NotNull(message = "Type missing.")
    private Type type;
    private String plot;
    private boolean hidden;
    private boolean scrapeImdb;
}
