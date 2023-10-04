package nl.nielsvanbruggen.videostreamingplatform.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.global.exception.GlobalExceptionHandler;
import nl.nielsvanbruggen.videostreamingplatform.global.exception.InvalidTokenException;
import nl.nielsvanbruggen.videostreamingplatform.invitetoken.exception.MissingInviteTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam(required = false) String token, @Valid @RequestBody RegisterRequest registerRequest, Authentication authentication) {
        try {
            return ResponseEntity.ok(service.register(registerRequest, token, authentication));
        } catch (MissingInviteTokenException ex) {
            return new ResponseEntity<>(GlobalExceptionHandler.singleMessageToErrorMap(ex.getMessage()), HttpStatus.BAD_REQUEST);
        } catch(InvalidTokenException ex) {
            return new ResponseEntity<>(GlobalExceptionHandler.singleMessageToErrorMap(ex.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        try {
            return ResponseEntity.ok(service.authenticate(request));
        } catch (AuthenticationException ex) {
            return new ResponseEntity<>(GlobalExceptionHandler.singleMessageToErrorMap("Username or password is wrong."), HttpStatus.BAD_REQUEST);
        }
    }
}
