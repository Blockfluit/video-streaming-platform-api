package nl.nielsvanbruggen.videostreamingplatform.global.exception;

public class AlreadyInUseException extends RuntimeException{
    public AlreadyInUseException(String message) {
        super(message);
    }
}
