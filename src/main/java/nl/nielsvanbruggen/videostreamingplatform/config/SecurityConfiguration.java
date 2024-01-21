package nl.nielsvanbruggen.videostreamingplatform.config;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CorsConfigurationSource configurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(configurer -> configurer.configurationSource(configurationSource))
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/request").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/tickets").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/actors").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/actors").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/genres").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/genres").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/media").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/media/*").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/media/*").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/users").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/auth/refresh-token/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/auth/refresh-token/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/v1/invite-tokens").hasAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/v1/stream/video-token").authenticated()
                        .requestMatchers("/api/v1/stream/**").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/change-password").permitAll()
//                        .requestMatchers("/v3/**").permitAll()
//                        .requestMatchers("/swagger-ui/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(customizer -> customizer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
