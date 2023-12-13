package nl.nielsvanbruggen.videostreamingplatform.user.controller;

import lombok.Builder;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.user.dto.UserDTO;

import java.util.List;

@Data
@Builder
public class AllUsersGetResponse {
    List<UserDTO> allUsers;
}
