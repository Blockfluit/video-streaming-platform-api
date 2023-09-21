package nl.nielsvanbruggen.videostreamingplatform.media.service;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.actor.model.Actor;
import nl.nielsvanbruggen.videostreamingplatform.actor.model.MediaActor;
import nl.nielsvanbruggen.videostreamingplatform.actor.repository.ActorRepository;
import nl.nielsvanbruggen.videostreamingplatform.actor.repository.MediaActorRepository;
import nl.nielsvanbruggen.videostreamingplatform.genre.Genre;
import nl.nielsvanbruggen.videostreamingplatform.genre.GenreRepository;
import nl.nielsvanbruggen.videostreamingplatform.genre.MediaGenre;
import nl.nielsvanbruggen.videostreamingplatform.genre.MediaGenreRepository;
import nl.nielsvanbruggen.videostreamingplatform.global.service.VideoService;
import nl.nielsvanbruggen.videostreamingplatform.media.controller.MediaPostRequest;
import nl.nielsvanbruggen.videostreamingplatform.media.dto.*;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.*;
import nl.nielsvanbruggen.videostreamingplatform.global.service.ImageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final MediaRepository mediaRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;
    private final VideoRepository videoRepository;
    private final SubtitleRepository subtitleRepository;
    private final MediaGenreRepository mediaGenreRepository;
    private final MediaActorRepository mediaActorRepository;
    private final MediaDTOGeneralMapper mediaDTOGeneralMapper;
    private final MediaDTOSpecificMapper mediaDTOSpecificMapper;
    private final VideoService videoService;
    private final ImageService imageService;
    @Value("${env.videos.root}")
    private String videosRoot;
    @Value("${env.ffprobe.path}")
    private String ffprobePath;

    public List<?> getMedia(Long id) {
        if(id != null) {
            return List.of(mediaRepository.findById(id)
                    .map(mediaDTOSpecificMapper)
                    .orElseThrow(() -> new IllegalArgumentException("Id does not exist.")));
        }

        return mediaRepository.findAll().stream()
                .map(mediaDTOGeneralMapper)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> postMedia(MediaPostRequest request) {
        // TODO: check if more validation is needed
        if(request.getThumbnail() == null) {
            throw new IllegalArgumentException("No thumbnail provided.");
        }
        if(!List.of("png", "jpg", "jpeg").contains(FilenameUtils.getExtension(request.getThumbnail().getOriginalFilename()))) {
            throw new IllegalArgumentException("Invalid file extension.");
        }

        if(mediaRepository.findByName(request.getName()).isPresent()) {
            throw new IllegalArgumentException("Media already exists.");
        }

        List<Genre> genres = genreRepository.findAllById(request.getGenres());
        if(genres.size() < request.getGenres().size()) {
            throw new IllegalArgumentException("Not all genres exist in the database.");
        }

        List<Actor> actors = actorRepository.findAllById(request.getActors());
        if (actors.size() < request.getActors().size()) {
            throw new IllegalArgumentException("Not all actors exist in the database.");
        }

        String imageName = request.getName() + "_" + request.getYear() + ".jpg";
        try {
            imageService.saveImage(request.getThumbnail().getInputStream(), imageName);
        } catch (IOException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Media media = Media.builder()
                .name(request.getName())
                .trailer(request.getTrailer())
                .thumbnail(imageName)
                .plot(request.getPlot())
                .type(request.getType())
                .year(request.getYear())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        mediaRepository.save(media);
        videoService.readVideos(media);
        genres.forEach(genre -> mediaGenreRepository.save(new MediaGenre(media, genre)));
        actors.forEach(actor -> mediaActorRepository.save(new MediaActor(media, actor)));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
