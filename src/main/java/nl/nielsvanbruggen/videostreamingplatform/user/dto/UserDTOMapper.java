package nl.nielsvanbruggen.videostreamingplatform.user.dto;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.auth.repository.RefreshTokenRepository;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.watched.dto.WatchedDTOMapper;
import nl.nielsvanbruggen.videostreamingplatform.watched.repository.WatchedRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserDTOMapper implements Function<User, UserDTO> {
    private final WatchedRepository watchedRepository;
    private final WatchedDTOMapper watchedDTOMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public UserDTO apply(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .lastLoginAt(user.getLastLoginAt())
                .lastActiveAt(user.getLastActiveAt())
                .createdAt(user.getCreatedAt())
                .lastWatched(
                        watchedRepository.findLastWatchedByUser(user, Pageable.ofSize(10)).stream()
                                .map(watchedDTOMapper)
                                .collect(Collectors.toList()))
                .refreshTokens(
                        refreshTokenRepository.findAllByUser(user))
                .build();
    }
}
