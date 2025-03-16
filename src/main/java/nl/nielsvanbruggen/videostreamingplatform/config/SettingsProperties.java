package nl.nielsvanbruggen.videostreamingplatform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Data
@Component
@ConfigurationProperties(prefix = "settings")
public class SettingsProperties {
    private Stream stream;
    private JWT jwt;

    @Data
    public static class Stream {
        private long maxChunkSizeKB;
    }

    @Data
    public static class JWT {
        private String secret;
        private Duration expiration;
    }
}
