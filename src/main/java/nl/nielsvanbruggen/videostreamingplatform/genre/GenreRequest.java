package nl.nielsvanbruggen.videostreamingplatform.genre;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenreRequest {
    @NotBlank
    private String genre;
}
