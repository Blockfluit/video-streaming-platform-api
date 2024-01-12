package nl.nielsvanbruggen.videostreamingplatform.user.controller;

import lombok.Builder;
import lombok.Data;
import nl.nielsvanbruggen.videostreamingplatform.user.dto.UserActivityCountByHourDTO;

import java.util.List;

@Data
@Builder
public class UserActivityGetResponse {
    private final List<UserActivityCountByHourDTO> content;
}
