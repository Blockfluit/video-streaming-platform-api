package nl.nielsvanbruggen.videostreamingplatform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("snapshot")
public class SnapshotProperties {
    private int width;
    private int height;
}
