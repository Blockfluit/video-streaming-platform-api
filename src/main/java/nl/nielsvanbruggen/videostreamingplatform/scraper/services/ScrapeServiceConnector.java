package nl.nielsvanbruggen.videostreamingplatform.scraper.services;

import lombok.extern.slf4j.Slf4j;
import nl.nielsvanbruggen.videostreamingplatform.config.ScraperProperties;
import nl.nielsvanbruggen.videostreamingplatform.scraper.models.ImdbTitle;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class ScrapeServiceConnector {
    private final WebClient.Builder webClientBuilder;

    public ScrapeServiceConnector(ScraperProperties scraperProperties) {
        this.webClientBuilder = WebClient.builder()
                .baseUrl(scraperProperties.getUrl());
    }

    public ImdbTitle getImdbTitle(String imdbId) {
        log.info("Fetching title: {} from scrape service", imdbId);

    return webClientBuilder.build()
                .get()
                .uri("/imdb/title/" + imdbId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ImdbTitle.class)
                .block();
    }
}
