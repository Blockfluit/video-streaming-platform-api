package nl.nielsvanbruggen.videostreamingplatform.recommendation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping
    public ResponseEntity<?> getRecommendations(Authentication authentication) {
        return new ResponseEntity<>(recommendationService.getRecommendations(authentication), HttpStatus.OK);
    }
}
