package nl.nielsvanbruggen.videostreamingplatform.auth;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.config.JwtService;
import nl.nielsvanbruggen.videostreamingplatform.global.exception.AlreadyInUseException;
import nl.nielsvanbruggen.videostreamingplatform.global.exception.InvalidTokenException;
import nl.nielsvanbruggen.videostreamingplatform.invitetoken.exception.MissingInviteTokenException;
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

    public AuthenticationResponse register(String token, RegisterRequest request, Authentication authentication) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AlreadyInUseException("Username already in use.");
        }
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AlreadyInUseException("Email already in use.");
        }

        Map<String, Object> extraClaims = new HashMap<>();

        if(authentication == null) {
            if(token == null) {
                throw new MissingInviteTokenException();
            }

            inviteTokenRepository.findById(token)
                .ifPresentOrElse(
                        (inviteToken) -> {
                            extraClaims.put("role", inviteToken.getRole());
                            inviteTokenRepository.delete(inviteToken);
                        },
                        () -> {
                            throw new InvalidTokenException();
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

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        String jwtToken = jwtService.generateToken(extraClaims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}