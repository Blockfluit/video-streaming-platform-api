package nl.nielsvanbruggen.videostreamingplatform.passwordreset;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/change-password")
@RequiredArgsConstructor
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    @PostMapping
    public ResponseEntity<Void> createToken(@Valid @RequestBody PasswordResetPostRequest passwordResetPostRequest) {
        passwordResetService.createToken(passwordResetPostRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Void> changePassword(@RequestParam(required = false) String token, @Valid @RequestBody PasswordResetPatchRequest passwordResetPatchRequest) {
        passwordResetService.changePassword(passwordResetPatchRequest, token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
