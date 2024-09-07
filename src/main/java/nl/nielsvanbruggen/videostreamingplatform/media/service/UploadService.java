package nl.nielsvanbruggen.videostreamingplatform.media.service;

import com.sun.jdi.InternalException;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.genre.Genre;
import nl.nielsvanbruggen.videostreamingplatform.genre.GenreRepository;
import nl.nielsvanbruggen.videostreamingplatform.genre.MediaGenre;
import nl.nielsvanbruggen.videostreamingplatform.genre.MediaGenreRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.controller.MediaPostRequest;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.MediaRepository;
import nl.nielsvanbruggen.videostreamingplatform.person.model.ContextRole;
import nl.nielsvanbruggen.videostreamingplatform.person.model.MediaPerson;
import nl.nielsvanbruggen.videostreamingplatform.person.model.Person;
import nl.nielsvanbruggen.videostreamingplatform.person.repository.MediaPersonRepository;
import nl.nielsvanbruggen.videostreamingplatform.person.service.PersonService;
import nl.nielsvanbruggen.videostreamingplatform.scraper.models.ImdbName;
import nl.nielsvanbruggen.videostreamingplatform.scraper.models.ImdbTitle;
import nl.nielsvanbruggen.videostreamingplatform.scraper.services.ScrapeServiceConnector;
import nl.nielsvanbruggen.videostreamingplatform.service.ImageService;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.video.service.VideoService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final ScrapeServiceConnector scrapeServiceConnector;
    private final ImageService imageService;
    private final MediaRepository mediaRepository;
    private final GenreRepository genreRepository;
    private final MediaGenreRepository mediaGenreRepository;
    private final VideoService videoService;
    private final MediaPersonRepository mediaPersonRepository;
    private final PersonService personService;

    @Transactional
    public void imdbUpload(MediaPostRequest request, User user) {
        ImdbTitle title = scrapeServiceConnector.getImdbTitle(request.getImdbId());

        Media media = Media.builder()
                .name(request.getName())
                .imdbId(request.getImdbId())
                .trailer(request.getTrailer())
                .thumbnail(handleThumbnail(request.getThumbnail(), request.getName() + "_" + title.getReleaseYear()))
                .type(request.getType())
                .createdBy(user)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .hidden(request.isHidden())
                .plot(title.getDescription())
                .year(title.getReleaseYear())
                .imdbRating(title.getImdbRating())
                .imdbRatingsAmount(title.getImdbRatingsAmount())
                .build();

        mediaRepository.saveAndFlush(media);

        handleGenres(media, title.getGenres());
        handleImdbNames(media, title.getCreators(), ContextRole.CREATOR);
        handleImdbNames(media, title.getDirectors(), ContextRole.DIRECTOR);
        handleImdbNames(media, title.getWriters(), ContextRole.WRITER);
        handleImdbNames(media, title.getStars(), ContextRole.STAR);
        handleImdbNames(media, title.getCast(), ContextRole.CAST);

        videoService.updateVideos(media);
    }

    @Transactional
    public void defaultUpload(MediaPostRequest request, User user) {
        Media media = Media.builder()
                .name(request.getName())
                .imdbId(request.getImdbId())
                .thumbnail(handleThumbnail(request.getThumbnail(), request.getName() + "_" + request.getYear()))
                .type(request.getType())
                .createdBy(user)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .hidden(request.isHidden())
                .plot(request.getPlot())
                .year(request.getYear())
                .build();

        mediaRepository.saveAndFlush(media);

        handleGenres(media, request.getGenres());

        videoService.updateVideos(media);
    }

    public String handleThumbnail(String thumbnail, String imageName) {
        try {
            String[] parts = thumbnail.split(",");
            InputStream is = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(parts[1]));

            return imageService.saveImage(is, imageName);
        } catch (IOException ex) {
            throw new InternalException("Saving thumbnail went wrong.");
        }
    }

    public List<Genre> handleGenres(Media media, Set<String> genres) {
        List<Genre> tmp = genres.stream()
                .map(Genre::new)
                .toList();

        List<Genre> dbGenres = genreRepository.saveAll(tmp);

        mediaGenreRepository.saveAll(
                dbGenres.stream()
                        .map(genre -> MediaGenre.builder()
                                .genre(genre)
                                .media(media)
                                .build())
                        .toList()
        );

        return dbGenres;
    }

    public List<Person> handleImdbNames(Media media, List<ImdbName> names, ContextRole role) {
        List<Person> tmp = names.stream()
                .map(name -> Person.builder()
                        .imdbId(name.getImdbId())
                        .firstname(name.getFirstname())
                        .lastname(name.getLastname())
                        .description(name.getDescription())
                        .dateOfBirth(name.getDateOfBirth())
                        .dateOfDeath(name.getDateOfDeath())
                        .build())
                .toList();

        List<Person> dbPersons = personService.saveAll(tmp);

        mediaPersonRepository.saveAll(
                dbPersons.stream()
                        .map(person -> MediaPerson.builder()
                                .person(person)
                                .media(media)
                                .contextRole(role)
                                .build())
                        .toList()
        );

        return dbPersons;
    }
}
