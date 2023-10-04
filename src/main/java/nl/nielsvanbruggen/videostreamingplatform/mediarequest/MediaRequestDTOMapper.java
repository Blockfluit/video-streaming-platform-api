package nl.nielsvanbruggen.videostreamingplatform.mediarequest;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class MediaRequestDTOMapper implements Function<MediaRequest, MediaRequestDTO> {
    @Override
    public MediaRequestDTO apply(MediaRequest mediaRequest) {
        return new MediaRequestDTO(
                mediaRequest.getId(),
                mediaRequest.getName(),
                mediaRequest.getYear(),
                mediaRequest.getComment(),
                mediaRequest.getCreatedAt(),
                mediaRequest.getCreatedBy().getUsername(),
                mediaRequest.getStatus()
        );
    }
}
