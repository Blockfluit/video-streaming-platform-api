package nl.nielsvanbruggen.videostreamingplatform.auth.service;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.auth.model.RefreshToken;
import nl.nielsvanbruggen.videostreamingplatform.auth.repository.RefreshTokenRepository;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public List<RefreshToken> getRefreshTokens(User user) {
        return refreshTokenRepository.findAllByUser(user);
    }

    public boolean isTokenValid(RefreshToken refreshToken) {
        return Instant.now()
                .isBefore(refreshToken.getExpiration());
    }

    public RefreshToken createRefreshToken(User user) {
        refreshTokenRepository.deleteAllByUser(user);

        RefreshToken token = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(Instant.now())
                .expiration(Instant.now().plus(EXPIRATION_TIME_IN_DAYS, ChronoUnit.DAYS))
                .user(user)
                .build();

        return refreshTokenRepository.save(token);
    }

    public void revokeRefreshToken(Long userId) {
        refreshTokenRepository.deleteAllByUserId(userId);
    }
}
