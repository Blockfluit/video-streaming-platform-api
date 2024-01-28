package nl.nielsvanbruggen.videostreamingplatform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "env")
public class EnvironmentProperties {
    private String secretKey;
    private Map<String, String> videos;
    private Map<String, String> thumbnail;
    private Map<String, String> snapshot;
    private Map<String, String> ffprobe;
    private Map<String, String> ffmpeg;
}
