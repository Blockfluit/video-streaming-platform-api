package nl.nielsvanbruggen.videostreamingplatform.genre;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AllGenresGetResponse {
    List<Genre> allGenres;
}
