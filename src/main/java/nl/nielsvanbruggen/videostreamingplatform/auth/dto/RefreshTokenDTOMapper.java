package nl.nielsvanbruggen.videostreamingplatform.auth.dto;

import nl.nielsvanbruggen.videostreamingplatform.auth.model.RefreshToken;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RefreshTokenDTOMapper implements Function<RefreshToken, RefreshTokenDTO> {

    @Override
    public RefreshTokenDTO apply(RefreshToken refreshToken) {
        return RefreshTokenDTO.builder()
                .id(refreshToken.getId())
                .token(refreshToken.getToken())
                .expiration(refreshToken.getExpiration())
                .createdAt(refreshToken.getCreatedAt())
                .build();
    }
}
