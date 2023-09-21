package nl.nielsvanbruggen.videostreamingplatform.passwordreset;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.global.exception.InvalidTokenException;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserRepository;
import nl.nielsvanbruggen.videostreamingplatform.global.util.TokenGeneratorUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final List<PasswordResetToken> passwordResetTokens;

    // Does not return anything for security reasons.
    public void createToken(PasswordResetPostRequest passwordResetPostRequest) {
        Optional<User> user = userRepository.findByEmail(passwordResetPostRequest.getEmail());

        if(user.isPresent()) {
//            passwordResetTokenRepository.deleteAllByUser(user.get());
            passwordResetTokens.stream()
                    .filter(token -> token.getUser()
                            .equals(user.get()))
                    .forEach(passwordResetTokens::remove);

            PasswordResetToken token = PasswordResetToken.builder()
                    .token(TokenGeneratorUtil.generate(64))
                    .createdAt(Instant.now())
                    .expiration(Instant.now().plus(30, ChronoUnit.MINUTES))
                    .user(user.get())
                    .build();

            passwordResetTokens.add(token);
//            passwordResetTokenRepository.save(token);
            System.out.println(token.getToken());
            // TODO: forward token to given email. (reset-endpoint + ?token={token})
        }
    }

    public void changePassword(String token, PasswordResetPatchRequest passwordResetPatchRequest) {
//        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findById(token)
//                .orElseThrow(InvalidTokenException::new);
//        passwordResetTokenRepository.delete(passwordResetToken);

        PasswordResetToken passwordResetToken = passwordResetTokens.stream()
                .filter(resetToken -> resetToken.getToken()
                        .equals(token))
                .findFirst()
                .orElseThrow(InvalidTokenException::new);
        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(passwordResetPatchRequest.getPassword()));
        userRepository.save(user);
    }
}
