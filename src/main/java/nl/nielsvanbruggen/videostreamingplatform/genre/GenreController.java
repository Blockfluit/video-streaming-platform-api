package nl.nielsvanbruggen.videostreamingplatform.genre;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<AllGenresGetResponse> getAllGenres() {
        AllGenresGetResponse response = AllGenresGetResponse.builder()
                .allGenres(genreService.getGenres())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> postGenre(@Valid @RequestBody GenreRequest genreRequest) {
        genreService.postGenre(genreRequest.getGenre());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{genre}")
    public ResponseEntity<String> deleteGenre(@PathVariable String genre) {
        genreService.deleteGenre(genre);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
