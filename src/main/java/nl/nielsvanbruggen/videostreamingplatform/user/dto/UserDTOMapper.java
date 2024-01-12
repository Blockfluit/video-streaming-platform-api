package nl.nielsvanbruggen.videostreamingplatform.user.dto;

import lombok.RequiredArgsConstructor;
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

    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getLastActiveAt(),
                user.getLastLoginAt(),
                user.getCreatedAt(),
                watchedRepository.findLastWatchedByUser(user, Pageable.ofSize(10)).stream()
                        .map(watchedDTOMapper)
                        .collect(Collectors.toList())
        );
    }
}
