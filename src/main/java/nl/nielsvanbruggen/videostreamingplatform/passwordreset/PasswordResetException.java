package nl.nielsvanbruggen.videostreamingplatform.passwordreset;

public class PasswordResetException extends RuntimeException{
    public PasswordResetException(String message) {
        super(message);
    }
}
