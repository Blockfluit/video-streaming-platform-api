package nl.nielsvanbruggen.videostreamingplatform.global.util;

public class MimeTypeUtil {
    private MimeTypeUtil() {}
    public static String getMimeType(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf('.') + 1);

        return switch (extension) {
            case "mp4" -> "video/mp4";
            case "jpg", "jpeg" -> "image/jpeg";
            case "vtt" -> "text/vtt";
            default -> "application/octet-stream";
        };
    }
}
