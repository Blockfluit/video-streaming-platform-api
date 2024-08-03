package nl.nielsvanbruggen.videostreamingplatform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "paths")
public class PathProperties {
    private Path videos;
    private Path thumbnail;
    private Path snapshot;

    @Data
    public static class Path {
        private String root;
    }
}
