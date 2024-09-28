package nl.nielsvanbruggen.videostreamingplatform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "scraper")
public class ScraperProperties {
    private String url;
}
