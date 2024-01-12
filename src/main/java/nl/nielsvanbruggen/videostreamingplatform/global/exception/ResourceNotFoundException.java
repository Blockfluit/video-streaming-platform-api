package nl.nielsvanbruggen.videostreamingplatform.global.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
