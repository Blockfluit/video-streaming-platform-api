package nl.nielsvanbruggen.videostreamingplatform.utils;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;

public class MimeTypeUtil {

    private MimeTypeUtil() {}

    public static String getMimeType(Path path) {
        String extension = FilenameUtils.getExtension(path.getFileName().toString());

        return switch (extension) {
            case "mp4" -> "video/mp4";
            case "jpg", "jpeg" -> "image/jpeg";
            case "vtt" -> "text/vtt";
            default -> "application/octet-stream";
        };
    }
}
