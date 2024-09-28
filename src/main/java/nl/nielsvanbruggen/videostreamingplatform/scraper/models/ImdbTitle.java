package nl.nielsvanbruggen.videostreamingplatform.scraper.models;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ImdbTitle {
    private String imdbId;
    private Double imdbRating;
    private Long imdbRatingsAmount;
    private String title;
    private Integer releaseYear;
    private Set<String> genres;
    private String description;
    private List<ImdbName> directors;
    private List<ImdbName> writers;
    private List<ImdbName> creators;
    private List<ImdbName> stars;
    private List<ImdbName> cast;
}
