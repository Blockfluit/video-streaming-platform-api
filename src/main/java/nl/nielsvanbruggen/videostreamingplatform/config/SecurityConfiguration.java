package nl.nielsvanbruggen.videostreamingplatform.config;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/change-password").permitAll()
                        .requestMatchers("/api/v1/stream/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/tickets").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/actors").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/actors").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/genres").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/genres").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/media").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/media/{id}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/media/{id}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/users").hasAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/v1/invite-tokens").hasAuthority(Role.ADMIN.name())
                        .anyRequest().authenticated())
                .sessionManagement(customizer -> customizer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
