package nl.nielsvanbruggen.videostreamingplatform.recommendation;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.service.UserService;
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
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Recommendation> getRecommendations(Authentication authentication) {
        User user = userService.getUser(authentication.getName());

        return new ResponseEntity<>(recommendationService.getRecommendations(user), HttpStatus.OK);
    }
}
