package nl.nielsvanbruggen.videostreamingplatform.media.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.model.MediaRelation;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.MediaRelationRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.MediaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/media-relation")
public class MediaRelationController {
    private final MediaRelationRepository mediaRelationRepository;
    private final MediaRepository mediaRepository;

    @PostMapping
    public ResponseEntity<Void> postRelation(@Valid @RequestBody MediaRelationPostRequestDto request) {
        Optional<Media> mediaFrom = mediaRepository.findById(request.getMediaFrom());
        Optional<Media> mediaTo = mediaRepository.findById(request.getMediaTo());

        if(mediaFrom.isEmpty() || mediaTo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        MediaRelation mediaRelation = MediaRelation.builder()
                .mediaFrom(mediaFrom.get())
                .mediaTo(mediaTo.get())
                .type(request.getType())
                .build();

        mediaRelationRepository.save(mediaRelation);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateRelation(@PathVariable Long id, @RequestBody MediaRelationPatchRequestDto request) {
        Optional<MediaRelation> mediaRelationOptional = mediaRelationRepository.findById(id);

        if(mediaRelationOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        MediaRelation mediaRelation = mediaRelationOptional.get();

        mediaRepository.findById(request.getMediaFrom()).ifPresent(mediaRelation::setMediaFrom);
        mediaRepository.findById(request.getMediaTo()).ifPresent(mediaRelation::setMediaTo);
        if(request.getType() != null) mediaRelation.setType(request.getType());

        mediaRelationRepository.save(mediaRelation);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelation(@PathVariable Long id) {
        if(mediaRelationRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        mediaRelationRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
