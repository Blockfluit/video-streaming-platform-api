package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RatingPostRequest {
    @NotNull
    @Min(0)
    @Max(10)
    private Double rating;

}
