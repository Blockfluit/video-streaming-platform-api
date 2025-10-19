package nl.nielsvanbruggen.videostreamingplatform.media.dto;

import lombok.Builder;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.media.model.MediaType;
import nl.nielsvanbruggen.videostreamingplatform.person.model.Person;
import nl.nielsvanbruggen.videostreamingplatform.video.dto.VideoDTO;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class MediaDTO {
    private long id;
    private String imdbId;
    private Double imdbRating;
    private Long imdbRatingsAmount;
    private String name;
    private String thumbnail;
    private String trailer;
    private String plot;
    private MediaType mediaType;
    private int year;
    private boolean hidden;
    private Instant updatedAt;
    private Instant createdAt;
    private int videoCount;
    private List<String> genres;
    private List<Person> directors;
    private List<Person> writers;
    private List<Person> creators;
    private List<Person> stars;
    private List<Person> cast;
    private List<VideoDTO> videos;
    private List<ReviewDTO> reviews;
    private int views;
    private List<RatingDTO> ratings;
    private double avgRating;
    private List<MediaRelationDto> relations;
}
