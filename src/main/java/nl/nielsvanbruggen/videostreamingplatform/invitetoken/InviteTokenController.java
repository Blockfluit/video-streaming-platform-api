package nl.nielsvanbruggen.videostreamingplatform.invitetoken;

import com.sun.jdi.InternalException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.exception.ErrorInfo;
import nl.nielsvanbruggen.videostreamingplatform.exception.InvalidTokenException;
import nl.nielsvanbruggen.videostreamingplatform.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/invite-tokens")
public class InviteTokenController {
    private final InviteTokenService inviteTokenService;
    private final InviteTokenDTOMapper inviteTokenDTOMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Map<String, List<InviteTokenDTO>>> getAllInviteTokens() {
        var inviteTokens = inviteTokenService.getAllInviteTokens().stream()
                        .map(inviteTokenDTOMapper)
                        .toList();

        return ResponseEntity.ok(Map.of("inviteTokens", inviteTokens));
    }

    @PostMapping
    public ResponseEntity<Void> createInviteToken(@RequestBody InviteTokenPostRequest inviteTokenPostRequest, Authentication authentication) {
        inviteTokenService.createInviteToken(inviteTokenPostRequest, userService.getUser(authentication));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{token}")
    public ResponseEntity<Void> deleteInviteToken(@PathVariable String token) {
        inviteTokenService.deleteInviteToken(token);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<ErrorInfo> handleInvalidTokenException(HttpServletRequest req, InvalidTokenException invalidTokenException) {
        return ResponseEntity.notFound().build();
    }
}
