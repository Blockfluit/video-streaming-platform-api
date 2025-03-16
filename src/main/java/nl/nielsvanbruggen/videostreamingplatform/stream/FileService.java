package nl.nielsvanbruggen.videostreamingplatform.stream;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.config.PathProperties;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Subtitle;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class FileService {
    private final PathProperties pathProperties;

    public Resource getSubtitle(Subtitle subtitle) throws MalformedURLException {
        Path path = Path.of(pathProperties.getVideos().getRoot(), subtitle.getPath());

        return new UrlResource(path.toUri());
    }

    public Resource getThumbnail(Media media) throws MalformedURLException {
        Path path = Path.of(pathProperties.getThumbnail().getRoot(), media.getThumbnail());

        return new UrlResource(path.toUri());
    }

    public Resource getSnapshot(Video video) throws MalformedURLException {
        Path path = Path.of(pathProperties.getSnapshot().getRoot(), video.getSnapshot());

        return new UrlResource(path.toUri());
    }
}
