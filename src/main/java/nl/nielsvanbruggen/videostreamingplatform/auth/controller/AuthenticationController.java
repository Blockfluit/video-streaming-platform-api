package nl.nielsvanbruggen.videostreamingplatform.auth.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.auth.service.AuthenticationService;
import nl.nielsvanbruggen.videostreamingplatform.auth.service.JwtService;
import nl.nielsvanbruggen.videostreamingplatform.auth.model.RefreshToken;
import nl.nielsvanbruggen.videostreamingplatform.auth.exception.RefreshTokenException;
import nl.nielsvanbruggen.videostreamingplatform.auth.service.RefreshTokenService;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestParam(required = false) String token, @Valid @RequestBody RegisterRequest registerRequest, Authentication authentication) {
        User user = authenticationService.register(registerRequest, token, authentication);

        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .refreshToken(refreshTokenService.createRefreshToken(user).getToken())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        User user = authenticationService.authenticate(request.getUsername(), request.getPassword());
        Optional<RefreshToken> entry = refreshTokenService.getRefreshTokens(user).stream()
                .findFirst();

        RefreshToken refreshToken = entry.isEmpty() ||
                !refreshTokenService.isTokenValid(entry.get()) ?
                refreshTokenService.createRefreshToken(user) :
                entry.get();

        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .refreshToken(refreshToken.getToken())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> getRefreshToken(@RequestParam String token) {
        RefreshToken refreshToken = refreshTokenService.getRefreshToken(token)
                .orElseThrow(() -> new RefreshTokenException("Token does not exist."));

        if (!refreshTokenService.isTokenValid(refreshToken)) {
            throw new RefreshTokenException("Token expired.");
        }

        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(refreshToken.getUser()))
                .refreshToken(refreshToken.getToken())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Transactional
    @DeleteMapping("/refresh-token/{id}")
    public ResponseEntity<Void> deleteRefreshToken(@PathVariable Long id) {
        refreshTokenService.revokeRefreshToken(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
