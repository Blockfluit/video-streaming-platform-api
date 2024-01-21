package nl.nielsvanbruggen.videostreamingplatform.watched.controller;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WatchedPostRequest {
    Long id;
    @NotNull
    Float timestamp;
}
