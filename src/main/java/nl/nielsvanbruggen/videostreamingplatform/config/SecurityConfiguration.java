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
import org.springframework.web.cors.CorsConfigurationSource;

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
                        .requestMatchers(HttpMethod.DELETE, "/request").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PATCH, "/tickets").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/actors").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/actors").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/genres").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/genres").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/media").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/media/*").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PATCH, "/media/*").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/media/*").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/users").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/auth/refresh-token/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/auth/refresh-token/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers("/invite-tokens").hasAuthority(Role.ADMIN.name())
                        .requestMatchers("/media-relation").hasAuthority(Role.ADMIN.name())
                        .requestMatchers("/stream/video-token").authenticated()
                        .requestMatchers("/stream/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/change-password").permitAll()
                        .requestMatchers("/scraper").permitAll()
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
