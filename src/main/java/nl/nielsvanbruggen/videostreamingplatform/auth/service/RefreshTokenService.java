package nl.nielsvanbruggen.videostreamingplatform.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.auth.model.RefreshToken;
import nl.nielsvanbruggen.videostreamingplatform.auth.repository.RefreshTokenRepository;
import nl.nielsvanbruggen.videostreamingplatform.global.util.TokenGeneratorUtil;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final static int EXPIRATION_TIME_IN_DAYS = 7;
    private final RefreshTokenRepository refreshTokenRepository;

    public List<RefreshToken> getAllRefreshTokens() {
        return refreshTokenRepository.findAll();
    }

    public Optional<RefreshToken> getRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public boolean isTokenValid(RefreshToken refreshToken) {
        return Instant.now()
                .isBefore(refreshToken.getExpiration().plus(EXPIRATION_TIME_IN_DAYS, ChronoUnit.DAYS));
    }

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        refreshTokenRepository.deleteAllByUser(user);

        RefreshToken token = RefreshToken.builder()
                .token(TokenGeneratorUtil.generate(254))
                .createdAt(Instant.now())
                .expiration(Instant.now().plus(EXPIRATION_TIME_IN_DAYS, ChronoUnit.DAYS))
                .user(user)
                .build();

        return refreshTokenRepository.save(token);
    }

    public void revokeRefreshToken(User user) {
        refreshTokenRepository.deleteAllByUser(user);
    }
}
