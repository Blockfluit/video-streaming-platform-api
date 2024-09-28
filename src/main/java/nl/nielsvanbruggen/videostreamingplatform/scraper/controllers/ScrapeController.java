package nl.nielsvanbruggen.videostreamingplatform.scraper.controllers;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.scraper.models.ImdbSearchTitleResult;
import nl.nielsvanbruggen.videostreamingplatform.scraper.services.ScrapeServiceConnector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scraper")
@RequiredArgsConstructor
public class ScrapeController {
    private final ScrapeServiceConnector scrapeServiceConnector;

    @GetMapping("/search")
    public Map<String, List<ImdbSearchTitleResult>> getSearchResults(@RequestParam String title) {
        return Map.of("content", scrapeServiceConnector.getImdbSearchResults(title));
    }
}
