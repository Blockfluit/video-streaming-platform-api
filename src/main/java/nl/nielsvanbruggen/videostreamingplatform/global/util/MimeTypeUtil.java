package nl.nielsvanbruggen.videostreamingplatform.global.util;

import java.nio.file.Path;

public class MimeTypeUtil {
    private MimeTypeUtil() {}
    public static String getMimeType(Path path) {
        String extension = path.getFileName().toString()
                .substring(path.getFileName().toString().lastIndexOf('.') + 1);

        return switch (extension) {
            case "mp4" -> "video/mp4";
            case "jpg", "jpeg" -> "image/jpeg";
            case "vtt" -> "text/vtt";
            default -> "application/octet-stream";
        };
    }
}
