package nl.nielsvanbruggen.videostreamingplatform.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.auth.service.AuthenticationService;
import nl.nielsvanbruggen.videostreamingplatform.config.JwtService;
import nl.nielsvanbruggen.videostreamingplatform.auth.model.RefreshToken;
import nl.nielsvanbruggen.videostreamingplatform.auth.exception.RefreshTokenException;
import nl.nielsvanbruggen.videostreamingplatform.auth.service.RefreshTokenService;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestParam(required = false) String token, @Valid @RequestBody RegisterRequest registerRequest, Authentication authentication) {
        User user = service.register(registerRequest, token, authentication);

        AuthenticationResponse response =  AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .refreshToken(refreshTokenService.createRefreshToken(user).getToken())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        User user = service.authenticate(request);

        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .refreshToken(refreshTokenService.createRefreshToken(user).getToken())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestParam String token) {
        RefreshToken refreshToken = refreshTokenService.getRefreshToken(token)
                .orElseThrow(() -> new RefreshTokenException("Token does not exist."));

        if(!refreshTokenService.isTokenValid(refreshToken)) {
            throw new RefreshTokenException("Token expired.");
        }

        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(refreshToken.getUser()))
                .refreshToken(refreshToken.getToken())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
