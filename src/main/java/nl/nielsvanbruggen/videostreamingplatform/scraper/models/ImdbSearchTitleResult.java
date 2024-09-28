package nl.nielsvanbruggen.videostreamingplatform.scraper.models;

import lombok.Data;

@Data
public class ImdbSearchTitleResult {
    private String imdbId;
    private String title;
    private Integer releaseYear;
    private String thumbnail;
    private String type;
    private String description;
}
