package nl.nielsvanbruggen.videostreamingplatform.user.dto;

import nl.nielsvanbruggen.videostreamingplatform.user.model.UserActivityCountByHour;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserActivityCountByHourMapper implements Function<UserActivityCountByHour, UserActivityCountByHourDTO> {
    @Override
    public UserActivityCountByHourDTO apply(UserActivityCountByHour userActivityCountByHour) {
        return UserActivityCountByHourDTO.builder()
                .userCount(userActivityCountByHour.getUserCount())
                .timestamp(userActivityCountByHour.getTimestamp())
                .build();
    }
}
