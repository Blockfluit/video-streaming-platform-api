package nl.nielsvanbruggen.videostreamingplatform.video.dto;

public record SubtitleDTO(
        long id,
        String label,
        String srcLang,
        boolean defaultSub,
        long videoId
) {
}
