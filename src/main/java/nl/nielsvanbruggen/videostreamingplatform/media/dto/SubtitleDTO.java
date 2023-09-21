package nl.nielsvanbruggen.videostreamingplatform.media.dto;

public record SubtitleDTO(
        long id,
        String label,
        String srcLang,
        boolean defaultSub,
        long videoId
) {
}
