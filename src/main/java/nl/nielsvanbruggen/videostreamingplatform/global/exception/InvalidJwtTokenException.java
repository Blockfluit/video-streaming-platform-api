package nl.nielsvanbruggen.videostreamingplatform.global.exception;

public class InvalidJwtTokenException extends RuntimeException {
    public InvalidJwtTokenException(Throwable throwable) {
        super(throwable);
    }
}
