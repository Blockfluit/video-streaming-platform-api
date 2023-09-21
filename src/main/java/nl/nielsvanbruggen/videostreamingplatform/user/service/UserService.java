package nl.nielsvanbruggen.videostreamingplatform.user.service;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.controller.UserDeleteRequest;
import nl.nielsvanbruggen.videostreamingplatform.user.controller.UserPatchRequest;
import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void patchUser(UserPatchRequest request, Authentication authentication) {
        final String username = request.getUsername();
        final boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
        if(!authentication.getName().equals(username) &&
                !isAdmin) {
            throw new IllegalArgumentException("Insufficient permission.");
        }
        User user =  userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        if(request.getRole() != null &&
                isAdmin) {
            user.setRole(request.getRole());
        }
        if(request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if(request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);
    }

    public void deleteUser(UserDeleteRequest request, Authentication authentication) {
        final String username = request.getUsername();

        if(!authentication.getName().equals(username) &&
                !authentication.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()))) {
            throw new IllegalArgumentException("Insufficient permission.");
        }
        User user =  userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));
        userRepository.delete(user);
    }
}
