package nl.nielsvanbruggen.videostreamingplatform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "paths")
public class PathProperties {
    private Path videos;
    private Path thumbnail;
    private Path snapshot;
    private Path ffmpeg;
    private Path ffprobe;

    @Data
    public static class Path {
        private String root;
    }
}
