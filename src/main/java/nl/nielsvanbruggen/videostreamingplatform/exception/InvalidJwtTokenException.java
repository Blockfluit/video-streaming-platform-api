package nl.nielsvanbruggen.videostreamingplatform.exception;

public class InvalidJwtTokenException extends RuntimeException {
    public InvalidJwtTokenException(Throwable throwable) {
        super(throwable);
    }
}
