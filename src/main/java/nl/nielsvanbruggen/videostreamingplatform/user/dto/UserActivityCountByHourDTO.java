package nl.nielsvanbruggen.videostreamingplatform.user.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserActivityCountByHourDTO {
    private Instant timestamp;
    private Long userCount;
}
