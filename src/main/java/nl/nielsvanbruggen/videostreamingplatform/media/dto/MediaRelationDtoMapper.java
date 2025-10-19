package nl.nielsvanbruggen.videostreamingplatform.media.dto;

import nl.nielsvanbruggen.videostreamingplatform.media.model.MediaRelation;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class MediaRelationDtoMapper implements Function<MediaRelation, MediaRelationDto> {

    @Override
    public MediaRelationDto apply(MediaRelation mediaRelation) {
        return MediaRelationDto.builder()
                .id(mediaRelation.getId())
                .mediaFrom(mediaRelation.getMediaFrom().getId())
                .mediaTo(mediaRelation.getMediaTo().getId())
                .type(mediaRelation.getType())
                .build();
    }
}
