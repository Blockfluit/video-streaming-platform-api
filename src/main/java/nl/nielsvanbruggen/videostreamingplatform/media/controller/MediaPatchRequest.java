package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.annotations.IsBase64Image;
import nl.nielsvanbruggen.videostreamingplatform.media.model.IdIndex;
import nl.nielsvanbruggen.videostreamingplatform.media.model.MediaType;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class MediaPatchRequest {
    private String imdbId;
    private String trailer;
    @IsBase64Image
    private String thumbnail;
    private Set<String> genres = Set.of();
    // These sets will be appended to, so they have to be mutable.
    private Set<Long> directors = new HashSet<>();
    private Set<Long> writers = new HashSet<>();
    private Set<Long> creators = new HashSet<>();
    private Set<Long> stars = new HashSet<>();
    private Set<Long> cast = new HashSet<>();
    private Integer year;
    private MediaType mediaType;
    private String plot;
    private List<IdIndex> order;
    private boolean updateFiles;
    private boolean hidden;
    private boolean updateTimestamp;
    private boolean scrapeImdb;

    public void setDirectors(Collection<Long> ids) {
        this.directors = ids != null ? new HashSet<>(ids) : new HashSet<>();
    }

    public void setWriters(Collection<Long> ids) {
        this.writers = ids != null ? new HashSet<>(ids) : new HashSet<>();
    }

    public void setCreators(Collection<Long> ids) {
        this.creators = ids != null ? new HashSet<>(ids) : new HashSet<>();
    }

    public void setStars(Collection<Long> ids) {
        this.stars = ids != null ? new HashSet<>(ids) : new HashSet<>();
    }

    public void setCast(Collection<Long> ids) {
        this.cast = ids != null ? new HashSet<>(ids) : new HashSet<>();
    }
}
