package nl.nielsvanbruggen.videostreamingplatform.Watched;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class WatchedDTOMapper implements Function<Watched, WatchedDTO> {
    @Override
    public WatchedDTO apply(Watched watched) {
        return new WatchedDTO(
                watched.getVideo().getId(),
                watched.getTimestamp()
        );
    }
}
