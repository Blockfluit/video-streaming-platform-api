package nl.nielsvanbruggen.videostreamingplatform.media.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.nielsvanbruggen.videostreamingplatform.genre.Genre;
import nl.nielsvanbruggen.videostreamingplatform.media.controller.MediaPatchRequest;
import nl.nielsvanbruggen.videostreamingplatform.media.model.IdIndex;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.MediaRepository;
import nl.nielsvanbruggen.videostreamingplatform.person.model.ContextRole;
import nl.nielsvanbruggen.videostreamingplatform.person.model.MediaPerson;
import nl.nielsvanbruggen.videostreamingplatform.person.model.Person;
import nl.nielsvanbruggen.videostreamingplatform.person.repository.MediaPersonRepository;
import nl.nielsvanbruggen.videostreamingplatform.person.repository.PersonRepository;
import nl.nielsvanbruggen.videostreamingplatform.scraper.models.ImdbTitle;
import nl.nielsvanbruggen.videostreamingplatform.scraper.services.ScrapeServiceConnector;
import nl.nielsvanbruggen.videostreamingplatform.video.repository.VideoRepository;
import nl.nielsvanbruggen.videostreamingplatform.video.service.VideoService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateService {
    private final UploadService uploadService;
    private final ScrapeServiceConnector scrapeServiceConnector;
    private final MediaRepository mediaRepository;
    private final VideoRepository videoRepository;
    private final VideoService videoService;
    private final PersonRepository personRepository;
    private final MediaPersonRepository mediaPersonRepository;

    @Transactional
    public void updateImdb(Media media, MediaPatchRequest request) {
        ImdbTitle title = scrapeServiceConnector.getImdbTitle(request.getImdbId());

        media.setImdbId(request.getImdbId());
        media.setPlot(title.getDescription());
        media.setImdbRating(title.getImdbRating());
        media.setImdbRatingsAmount(title.getImdbRatingsAmount());
        media.setYear(title.getReleaseYear());

        List<Genre> dbGenres = uploadService.handleGenres(media, title.getGenres());
        List<Person> dbCreators = uploadService.handleImdbNames(media, title.getCreators(), ContextRole.CREATOR);
        List<Person> dbDirectors = uploadService.handleImdbNames(media, title.getDirectors(), ContextRole.DIRECTOR);
        List<Person> dbWriters = uploadService.handleImdbNames(media, title.getWriters(), ContextRole.WRITER);
        List<Person> dbStars = uploadService.handleImdbNames(media, title.getStars(), ContextRole.STAR);
        List<Person> dbCast = uploadService.handleImdbNames(media, title.getCast(), ContextRole.CAST);

        request.getGenres().addAll(dbGenres.stream().map(Genre::getName).toList());
        request.getCreators().addAll(dbCreators.stream().map(Person::getId).toList());
        request.getDirectors().addAll(dbDirectors.stream().map(Person::getId).toList());
        request.getWriters().addAll(dbWriters.stream().map(Person::getId).toList());
        request.getStars().addAll(dbStars.stream().map(Person::getId).toList());
        request.getCast().addAll(dbCast.stream().map(Person::getId).toList());

        updateDefault(media, request);
    }

    @Transactional
    public void updateDefault(Media media, MediaPatchRequest request) {
        if(request.getTrailer() != null) media.setTrailer(request.getTrailer());
        if(request.getPlot() != null) media.setPlot(request.getPlot());
        if(request.getMediaType() != null) media.setType(request.getMediaType());
        if(request.getYear() != null) media.setYear(request.getYear());
        if(request.isUpdateTimestamp()) media.setUpdatedAt(Instant.now());
        if(request.getThumbnail() != null) uploadService.handleThumbnail(request.getThumbnail(), media.getName() + "_" + request.getYear());
        media.setHidden(request.isHidden());

        if(request.getOrder() != null) {
            Map<Long, Integer> indexLookup = request.getOrder().stream()
                    .collect(Collectors.toMap(
                            IdIndex::getId,
                            IdIndex::getIndex
                    ));

            videoRepository.saveAll(
                    videoRepository.findAllById(request.getOrder().stream().map(IdIndex::getId).toList()).stream()
                            .peek(video -> video.setIndex(indexLookup.get(video.getId())))
                            .toList()
            );
        }

        if(request.isUpdateFiles()) {
            videoService.updateVideos(media);
        }

        handlePersons(media, request.getCast(), ContextRole.CAST);
        handlePersons(media, request.getStars(), ContextRole.STAR);
        handlePersons(media, request.getCreators(), ContextRole.CREATOR);
        handlePersons(media, request.getDirectors(), ContextRole.DIRECTOR);
        handlePersons(media, request.getWriters(), ContextRole.WRITER);

        mediaRepository.save(media);
    }

    private void handlePersons(Media media, Set<Long> personIds, ContextRole role) {
        log.debug("Handling persons ({}) with role: ({})", personIds.size(), role);

        List<Person> dbPersons = personRepository.findAllById(personIds);

        mediaPersonRepository.deleteAll(
                mediaPersonRepository.findAllByMediaAndContextRole(media, role).stream()
                        .filter(mediaPerson -> !personIds.contains(mediaPerson.getPerson().getId()))
                        .toList()
        );

         List<MediaPerson> mediaPersons = dbPersons.stream()
                 .map(person -> MediaPerson.builder()
                         .media(media)
                         .person(person)
                         .contextRole(role)
                         .build())
                 .toList();

         mediaPersonRepository.saveAll(mediaPersons);

        log.debug("Finished handling persons with role: ({})", role);
    }
}
