package nl.nielsvanbruggen.videostreamingplatform.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.user.service.UserActivityService;
import nl.nielsvanbruggen.videostreamingplatform.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserActivityService userActivityService;

    @GetMapping
    public ResponseEntity<AllUsersGetResponse> getAllUsers() {
        AllUsersGetResponse response = AllUsersGetResponse.builder()
                .allUsers(userService.getAllUsers())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Void> patchUser(@Valid @RequestBody UserPatchRequest userPatchRequest, Authentication authentication) {
        userService.patchUser(userPatchRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@Valid @RequestBody UserDeleteRequest userDeleteRequest, Authentication authentication) {
        userService.deleteUser(userDeleteRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/activity")
    public ResponseEntity<UserActivityGetResponse> getUserActivity() {
        UserActivityGetResponse response = UserActivityGetResponse.builder()
                .content(userActivityService.getAllUserActivity())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
     }
}
