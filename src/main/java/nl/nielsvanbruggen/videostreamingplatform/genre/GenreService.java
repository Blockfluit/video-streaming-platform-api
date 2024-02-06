package nl.nielsvanbruggen.videostreamingplatform.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public List<Genre> getGenres() {
        return genreRepository.findAll();
    }

    public void postGenre(String genre) {
        genreRepository.save(new Genre(genre));
    }

    public void deleteGenre(String genre) {
        Genre tempGenre = genreRepository.findById(genre)
                .orElseThrow(() -> new IllegalArgumentException("Genre does not exist."));
        genreRepository.delete(tempGenre);
    }
}
