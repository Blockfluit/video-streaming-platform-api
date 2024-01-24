package nl.nielsvanbruggen.videostreamingplatform.auth.service;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.auth.repository.controller.AuthenticationRequest;
import nl.nielsvanbruggen.videostreamingplatform.auth.repository.controller.RegisterRequest;
import nl.nielsvanbruggen.videostreamingplatform.config.JwtService;
import nl.nielsvanbruggen.videostreamingplatform.global.exception.AlreadyInUseException;
import nl.nielsvanbruggen.videostreamingplatform.global.exception.InvalidTokenException;
import nl.nielsvanbruggen.videostreamingplatform.invitetoken.InviteToken;
import nl.nielsvanbruggen.videostreamingplatform.invitetoken.InviteTokenRepository;
import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final InviteTokenRepository inviteTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public User register(RegisterRequest request, String token, Authentication authentication) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AlreadyInUseException("Username already in use.");
        }
        if(request.getEmail() != null &&
                userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AlreadyInUseException("Email already in use.");
        }

        Optional<InviteToken> inviteToken = inviteTokenRepository.findById(token);
        Role role;

        if(authentication == null && inviteToken.isPresent()) {
                if(inviteToken.get().isUsed() &&
                        !inviteToken.get().isMaster()) {
                    throw new InvalidTokenException("Token already used.");
                }

                if(inviteToken.get().getExpiration().isBefore(Instant.now())) {
                    throw new InvalidTokenException("Token expired.");
                }

                role = inviteToken.get().getRole();
                inviteToken.get().setUsed(true);
                inviteTokenRepository.save(inviteToken.get());
        }
        else if(authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()))) {
            role = request.getRole() == null ? Role.USER: request.getRole();
        }
        else {
            throw new InvalidTokenException("Token does not exist");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .lastLoginAt(Instant.now())
                .createdAt(Instant.now())
                .build();
        return userRepository.save(user);
    }

    public User authenticate(AuthenticationRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        user.setLastLoginAt(Instant.now());
        userRepository.save(user);

        return user;
    }
}
