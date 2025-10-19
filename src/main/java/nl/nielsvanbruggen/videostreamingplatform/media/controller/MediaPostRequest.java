package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.annotations.IsBase64Image;
import nl.nielsvanbruggen.videostreamingplatform.media.model.MediaType;

import java.util.Set;

@Data
@Builder
public class MediaPostRequest {
    @NotBlank(message = "Name missing.")
    private String name;
    private String imdbId;
    private String trailer;
    @IsBase64Image
    @NotNull
    private String thumbnail;
    @Builder.Default
    private Set<String> genres = Set.of();
    @Builder.Default
    private Set<Long> directors = Set.of();
    @Builder.Default
    private Set<Long> writers = Set.of();
    @Builder.Default
    private Set<Long> creators = Set.of();
    @Builder.Default
    private Set<Long> stars = Set.of();
    @Builder.Default
    private Set<Long> cast = Set.of();
    private Integer year;
    @NotNull(message = "Type missing.")
    private MediaType type;
    private String plot;
    private boolean hidden;
    private boolean scrapeImdb;
}
