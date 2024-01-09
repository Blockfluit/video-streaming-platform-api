package nl.nielsvanbruggen.videostreamingplatform.user.service;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.model.UserActivity;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserActivityRepository;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserActivityService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserActivityRepository userActivityRepository;
    private final static Map<String, Instant> userActivity = new ConcurrentHashMap<>();

    @Scheduled(cron = "0 0/15 * 1/1 * *")
    public void persistUserActivity() {
        userActivity.forEach((userName, timestamp) -> {
            if(timestamp.isBefore(Instant.now().plus(15, ChronoUnit.MINUTES))) {
                User user = userService.getUser(userName);
                UserActivity userActivity = UserActivity.builder()
                        .user(user)
                        .createdAt(Instant.now())
                        .build();
                userActivityRepository.save(userActivity);
            }
        });
    }

    public void recordUserActivity(Authentication authentication) {
        userActivity.put(authentication.getName(), Instant.now());

        User user = userService.getUser(authentication.getName());
        user.setLastActiveAt(Instant.now());
        userRepository.save(user);
    }
}
