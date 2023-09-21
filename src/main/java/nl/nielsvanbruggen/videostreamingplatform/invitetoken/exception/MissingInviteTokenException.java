package nl.nielsvanbruggen.videostreamingplatform.invitetoken.exception;

public class MissingInviteTokenException extends RuntimeException{
    public MissingInviteTokenException() {
        super("Invite token is missing.");
    }
    public MissingInviteTokenException(String message) {
        super(message);
    }
}
