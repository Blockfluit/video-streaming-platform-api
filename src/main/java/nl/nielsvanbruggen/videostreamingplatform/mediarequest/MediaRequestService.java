package nl.nielsvanbruggen.videostreamingplatform.mediarequest;

import com.sun.jdi.InternalException;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaRequestService {
    private final MediaRequestRepository mediaRequestRepository;
    private final UserRepository userRepository;
    private final MediaRequestDTOMapper mediaRequestDTOMapper;

    public List<MediaRequestDTO> getMediaRequest() {
        return mediaRequestRepository.findAll().stream()
                .map(mediaRequestDTOMapper)
                .collect(Collectors.toList());
    }

    public void postMediaRequest(MediaRequestPostRequest request, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new InternalException("No user associated with this name"));

        MediaRequest mediaRequest = MediaRequest.builder()
                .name(request.getName())
                .comment(request.getComment())
                .year(request.getYear())
                .status(Status.NEW)
                .createdBy(user)
                .updatedAt(Instant.now())
                .createdAt(Instant.now())
                .build();

        mediaRequestRepository.save(mediaRequest);
    }

    public void patchMediaRequest(Long id, MediaRequestPatchRequest request, Authentication authentication) {
        MediaRequest mediaRequest = mediaRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No request found with this id."));

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new InternalException("No user associated with this name."));

        if(!mediaRequest.getCreatedBy().equals(user) &&
                !user.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()))
        ) {
            throw new IllegalArgumentException("Insufficient permission.");
        }

        if(!mediaRequest.getStatus().equals(Status.NEW) &&
                !user.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()))
        ) {
            throw new IllegalArgumentException("Insufficient permission.");
        }

        if(user.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()))) {
            if(request.getStatus() != null) mediaRequest.setStatus(request.getStatus());
        }

        if(request.getName() != null) mediaRequest.setName(request.getName());
        if(request.getYear() != null) mediaRequest.setYear(request.getYear());
        if(request.getComment() != null) mediaRequest.setComment(request.getComment());

        mediaRequest.setUpdatedAt(Instant.now());

        mediaRequestRepository.save(mediaRequest);
    }

    public void deleteMediaRequest(Long id) {
        mediaRequestRepository.deleteById(id);
    }
}
