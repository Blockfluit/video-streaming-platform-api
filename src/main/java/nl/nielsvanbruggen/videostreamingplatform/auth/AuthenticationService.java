package nl.nielsvanbruggen.videostreamingplatform.auth;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.config.JwtService;
import nl.nielsvanbruggen.videostreamingplatform.global.exception.AlreadyInUseException;
import nl.nielsvanbruggen.videostreamingplatform.global.exception.InvalidTokenException;
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
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final InviteTokenRepository inviteTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request, String token, Authentication authentication) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AlreadyInUseException("Username already in use.");
        }
        if(request.getEmail() != null &&
                userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AlreadyInUseException("Email already in use.");
        }

        Map<String, Object> extraClaims = new HashMap<>();

        if(authentication == null) {
            inviteTokenRepository.findById(token)
                .ifPresentOrElse(
                        (inviteToken) -> {
                            if(inviteToken.isUsed() &&
                                    !inviteToken.isMaster()) {
                                throw new InvalidTokenException("Token already used.");
                            }

                            if(inviteToken.getExpiration().isBefore(Instant.now())) {
                                throw new InvalidTokenException("Token expired.");
                            }

                            extraClaims.put("role", inviteToken.getRole());

                            inviteToken.setUsed(true);
                            inviteTokenRepository.save(inviteToken);
                        },
                        () -> {
                            throw new InvalidTokenException("Token does not exist");
                        }
                );
        }
        if(authentication != null &&
                authentication.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()))) {
            Role role = request.getRole() == null ? Role.USER: request.getRole();
            extraClaims.put("role", role);
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role((Role) extraClaims.get("role"))
                .createdAt(Instant.now())
                .build();
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(extraClaims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        user.setLastLoginAt(Instant.now());
        userRepository.save(user);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        String jwtToken = jwtService.generateToken(extraClaims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
