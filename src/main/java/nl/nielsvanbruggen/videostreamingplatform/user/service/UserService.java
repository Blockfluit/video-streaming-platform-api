package nl.nielsvanbruggen.videostreamingplatform.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.controller.UserDeleteRequest;
import nl.nielsvanbruggen.videostreamingplatform.user.controller.UserPatchRequest;
import nl.nielsvanbruggen.videostreamingplatform.user.dto.UserDTO;
import nl.nielsvanbruggen.videostreamingplatform.user.dto.UserDTOMapper;
import nl.nielsvanbruggen.videostreamingplatform.user.exception.UserNotFoundException;
import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;
import nl.nielsvanbruggen.videostreamingplatform.user.model.User;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper;

    public User getUser(Authentication authentication) throws UserNotFoundException {
        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with name: " + username + " does not exist."));
    }

    public User getUser(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " does not exist."));
    }

    public User getUser(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with name: " + username + " does not exist."));
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userDTOMapper)
                .collect(Collectors.toList());
    }

    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.toString()));
    }

    public void patchUser(UserPatchRequest request, Authentication authentication) {
        final boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.name()));

        User user =  userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        if(!authentication.getName().equals(user.getUsername()) && !isAdmin) {
            throw new IllegalArgumentException("Insufficient permission.");
        }
        if(request.getRole() != null && isAdmin) user.setRole(request.getRole());
        if(request.getEmail() != null) user.setEmail(request.getEmail());
        if(request.getPassword() != null) user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }

    @Transactional
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
