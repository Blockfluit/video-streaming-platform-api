package nl.nielsvanbruggen.videostreamingplatform.exception;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException() {
        super("Invite token is invalid.");
    }
    public InvalidTokenException(String message) {
        super(message);
    }
}
