package nl.nielsvanbruggen.videostreamingplatform.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.service.UserActivityService;
import nl.nielsvanbruggen.videostreamingplatform.user.service.UserService;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LastActiveInterceptor implements HandlerInterceptor {
    private final UserActivityService userActivityService;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null &&
                !authentication.getName().equals("anonymousUser")) {
            userActivityService.recordUserActivity(authentication);
        }
        return true;
    }
}
