package nl.nielsvanbruggen.videostreamingplatform.media.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.nielsvanbruggen.videostreamingplatform.config.PathProperties;
import nl.nielsvanbruggen.videostreamingplatform.media.controller.MediaPostRequest;
import nl.nielsvanbruggen.videostreamingplatform.media.model.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static nl.nielsvanbruggen.videostreamingplatform.media.model.MediaType.MOVIE;
import static nl.nielsvanbruggen.videostreamingplatform.media.model.MediaType.SERIES;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndexService {

    private final PathProperties pathProperties;
    private final UploadService uploadService;
    private final MediaService mediaService;

    public void indexAll() {
        log.info("Indexing all new media");

        try (Stream<Path> pathStream = Files.walk(Paths.get(pathProperties.getVideos().getRoot()), 2, FileVisitOption.FOLLOW_LINKS)) {
            pathStream
                    .filter(path -> path.toFile().isDirectory() && !path.toFile().getName().toLowerCase().contains("videos"))
                    .filter(path -> !mediaService.existsByName(path.toFile().getName()))
                    .forEach(path -> {
                        // Create request

                        var request = MediaPostRequest.builder()
                                .name(path.toFile().getName())
                                .type(determineType(path))
                                .hidden(false)
                                .build();

                        log.info("Indexing: ({})", request.getName());
                        uploadService.defaultUpload(request, null);
                    });

        } catch (IOException e) {
            log.info("Something went wrong while indexing: ({})", e.getMessage());
        }

        log.info("Finished indexing all new media");
    }

    private MediaType determineType(Path path) {
        try(Stream<Path> pathStream = Files.walk(path, 1, FileVisitOption.FOLLOW_LINKS)) {
            // Check if there are subfolders with season name
            boolean isSeries = pathStream.anyMatch(p -> p.toFile().isDirectory() && p.toFile().getName().toLowerCase().contains("season"));

            return isSeries ? SERIES : MOVIE;
        } catch (IOException e) {
            log.info("Could not index media: ({})", e.getMessage());
        }

        return MOVIE;
    }
}
