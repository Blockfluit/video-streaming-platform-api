package nl.nielsvanbruggen.videostreamingplatform.global.exception;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ErrorInfo {
    private String url;
    private String message;
    private Instant timestamp;
}
