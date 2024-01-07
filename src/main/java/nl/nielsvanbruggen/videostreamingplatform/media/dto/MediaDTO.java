package nl.nielsvanbruggen.videostreamingplatform.media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.actor.dto.ActorDTO;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Type;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private List<ActorDTO> actors;
    private List<VideoDTO> videos;
    private List<ReviewDTO> reviews;
    private int views;
    private List<RatingDTO> ratings;
}
