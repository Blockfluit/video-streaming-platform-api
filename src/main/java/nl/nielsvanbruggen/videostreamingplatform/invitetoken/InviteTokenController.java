package nl.nielsvanbruggen.videostreamingplatform.invitetoken;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.global.exception.GlobalExceptionHandler;
import nl.nielsvanbruggen.videostreamingplatform.global.exception.InvalidTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/invite-tokens")
public class InviteTokenController {
    private final InviteTokenService inviteTokenService;


    @GetMapping
    public ResponseEntity<AllInviteTokensGetResponse> getAllInviteTokens() {
        AllInviteTokensGetResponse response = AllInviteTokensGetResponse.builder()
                .allInviteTokens(inviteTokenService.getAllInviteTokens())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createInviteToken(@RequestBody InviteTokenPostRequest inviteTokenPostRequest, Authentication authentication) {
        inviteTokenService.createInviteToken(inviteTokenPostRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{token}")
    public ResponseEntity<?> deleteInviteToken(@PathVariable String token) {
        try {
            inviteTokenService.deleteInviteToken(token);
            return ResponseEntity.ok().build();
        } catch(InvalidTokenException ex) {
            return new ResponseEntity<>(GlobalExceptionHandler.singleMessageToErrorMap(ex.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
