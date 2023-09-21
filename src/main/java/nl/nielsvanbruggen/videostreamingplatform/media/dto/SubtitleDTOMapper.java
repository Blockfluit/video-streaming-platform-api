package nl.nielsvanbruggen.videostreamingplatform.media.dto;

import nl.nielsvanbruggen.videostreamingplatform.media.model.Subtitle;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class SubtitleDTOMapper implements Function<Subtitle, SubtitleDTO> {
    @Override
    public SubtitleDTO apply(Subtitle subtitle) {
        return new SubtitleDTO(
                subtitle.getId(),
                subtitle.getLabel(),
                subtitle.getSrcLang(),
                subtitle.isDefaultSub(),
                subtitle.getVideo().getId()
        );
    }
}
