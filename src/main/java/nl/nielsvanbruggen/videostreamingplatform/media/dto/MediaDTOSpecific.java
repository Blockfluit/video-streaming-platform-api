package nl.nielsvanbruggen.videostreamingplatform.media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.actor.dto.ActorDTO;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Review;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Type;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Video;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaDTOSpecific {
    private long id;
    private String name;
    private String thumbnail;
    private String trailer;
    private String plot;
    private Type type;
    private int year;
    private Instant updatedAt;
    private Instant createdAt;
    private List<String> genre;
    private List<ActorDTO> actors;
    private List<VideoDTO> videos;
    private List<Review> reviews;
    private int views;
    private double rating;
}
