package nl.nielsvanbruggen.videostreamingplatform.scraper.services;

import lombok.extern.slf4j.Slf4j;
import nl.nielsvanbruggen.videostreamingplatform.config.ScraperProperties;
import nl.nielsvanbruggen.videostreamingplatform.scraper.models.ImdbTitle;
import nl.nielsvanbruggen.videostreamingplatform.scraper.models.ImdbSearchTitleResult;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
public class ScrapeServiceConnector {
    private final WebClient.Builder webClientBuilder;

    public ScrapeServiceConnector(ScraperProperties scraperProperties) {
        this.webClientBuilder = WebClient.builder()
                .baseUrl(scraperProperties.getUrl());
    }

    public ImdbTitle getImdbTitle(String imdbId) {
        log.info("Fetching title: ({}) from scrape service...", imdbId);

        return webClientBuilder.build()
                .get()
                .uri(builder -> builder
                        .pathSegment("imdb", "title", imdbId)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ImdbTitle.class)
                .doOnSuccess(res -> log.info("Successfully fetched title: ({})", imdbId))
                .doOnError(e -> log.error("Fetching title: ({}) went wrong!", imdbId, e))
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(10)))
                .block();
    }

    public List<ImdbSearchTitleResult> getImdbSearchResults(String search) {
        log.info("Fetching search results: ({}) from scrape service...", search);

        return webClientBuilder.build()
                .get()
                .uri(builder -> builder
                        .pathSegment("imdb", "search")
                        .queryParam("title", search)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(ImdbSearchTitleResult.class)
                .collectList()
                .doOnSuccess(res -> log.info("Successfully fetched title: ({})", search))
                .doOnError(e -> log.error("Fetching title: ({}) went wrong!", search, e))
                .block();
    }
}
