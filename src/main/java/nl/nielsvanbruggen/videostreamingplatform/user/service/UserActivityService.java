package nl.nielsvanbruggen.videostreamingplatform.user.service;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.model.UserActivity;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserActivityRepository;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserActivityService {
    private final UserRepository userRepository;
    private final UserActivityRepository userActivityRepository;

    public void recordUserActivity(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setLastActiveAt(Instant.now());
        userRepository.save(user);

        UserActivity userActivity = UserActivity.builder()
                .user(user)
                .createdAt(Instant.now())
                .build();
        userActivityRepository.save(userActivity);
    }
}
