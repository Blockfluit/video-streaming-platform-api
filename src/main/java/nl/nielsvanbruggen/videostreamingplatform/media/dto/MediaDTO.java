package nl.nielsvanbruggen.videostreamingplatform.media.dto;

import lombok.Builder;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.actor.model.Actor;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Type;
import nl.nielsvanbruggen.videostreamingplatform.video.dto.VideoDTO;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class MediaDTO {
    private long id;
    private String name;
    private String thumbnail;
    private String trailer;
    private String plot;
    private Type type;
    private int year;
    private Instant updatedAt;
    private Instant createdAt;
    private int videoCount;
    private List<String> genres;
    private List<Actor> actors;
    private List<VideoDTO> videos;
    private List<ReviewDTO> reviews;
    private int views;
    private List<RatingDTO> ratings;
    private double avgRating;
}
