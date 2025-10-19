package nl.nielsvanbruggen.videostreamingplatform.video.exception;

public class VideoException extends RuntimeException {
    public VideoException(String message) {
        super(message);
    }

    public VideoException(Throwable throwable) {
        super(throwable);
    }

    public VideoException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
