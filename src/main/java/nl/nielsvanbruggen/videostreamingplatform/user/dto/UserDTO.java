package nl.nielsvanbruggen.videostreamingplatform.user.dto;

import nl.nielsvanbruggen.videostreamingplatform.user.model.Role;

public record UserDTO(long id,
                      String username,
                      String email,
                      Role role) {
}
