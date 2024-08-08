package nl.nielsvanbruggen.videostreamingplatform.invitetoken;

import com.sun.jdi.InternalException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.exception.ErrorInfo;
import nl.nielsvanbruggen.videostreamingplatform.exception.InvalidTokenException;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/invite-tokens")
public class InviteTokenController {
    private final InviteTokenService inviteTokenService;
    private final InviteTokenDTOMapper inviteTokenDTOMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<AllInviteTokensGetResponse> getAllInviteTokens() {
        AllInviteTokensGetResponse response = AllInviteTokensGetResponse.builder()
                .allInviteTokens(inviteTokenService.getAllInviteTokens().stream()
                        .map(inviteTokenDTOMapper)
                        .toList())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createInviteToken(@RequestBody InviteTokenPostRequest inviteTokenPostRequest, Authentication authentication) {
        User user = userService.getUser(authentication.getName());
        inviteTokenService.createInviteToken(user, inviteTokenPostRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{token}")
    public ResponseEntity<Void> deleteInviteToken(@PathVariable String token) {
        inviteTokenService.deleteInviteToken(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<ErrorInfo> handleInvalidTokenException(HttpServletRequest req, InvalidTokenException invalidTokenException) {
        ErrorInfo info = ErrorInfo.builder()
                .url(req.getRequestURL().toString())
                .timestamp(Instant.now())
                .message(invalidTokenException.getMessage())
                .build();

        return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
    }
}
