package nl.nielsvanbruggen.videostreamingplatform.genre;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<List<Genre>> getGenres() {
        return new ResponseEntity<>(genreService.getGenres(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> postGenre(@Valid @RequestBody GenreRequest genreRequest) {
        genreService.postGenre(genreRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteGenre(@Valid @RequestBody GenreRequest genreRequest) {
        genreService.deleteGenre(genreRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
