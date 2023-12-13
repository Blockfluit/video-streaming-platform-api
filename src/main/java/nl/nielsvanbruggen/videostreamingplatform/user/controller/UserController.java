package nl.nielsvanbruggen.videostreamingplatform.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.dto.UserDTO;
import nl.nielsvanbruggen.videostreamingplatform.user.dto.UserDTOMapper;
import nl.nielsvanbruggen.videostreamingplatform.user.repository.UserRepository;
import nl.nielsvanbruggen.videostreamingplatform.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<AllUsersGetResponse> getAllUsers() {
        AllUsersGetResponse response = AllUsersGetResponse.builder()
                .allUsers(userService.getAllUsers())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<?> patchUser(@Valid @RequestBody UserPatchRequest userPatchRequest, Authentication authentication) {
        userService.patchUser(userPatchRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@Valid @RequestBody UserDeleteRequest userDeleteRequest, Authentication authentication) {
        userService.deleteUser(userDeleteRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
