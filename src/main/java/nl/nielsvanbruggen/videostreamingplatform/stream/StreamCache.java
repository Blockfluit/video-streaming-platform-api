package nl.nielsvanbruggen.videostreamingplatform.stream;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.global.exception.ResourceNotFoundException;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.MediaRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.SubtitleRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.VideoRepository;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class StreamCache {
    private final VideoRepository videoRepository;
    private final MediaRepository mediaRepository;
    private final SubtitleRepository subtitleRepository;
    private static final Map<Long, String> videoPaths = new ConcurrentHashMap<>();
    private static final Map<Long, String> thumbnailPaths = new ConcurrentHashMap<>();
    private static final Map<Long, String> snapshotPaths = new ConcurrentHashMap<>();
    private static final Map<Long, String> subtitlePaths = new ConcurrentHashMap<>();
    private static final int MAX_VIDEO_MAP_PATHS_SIZE = 1000;
    private static final int MAX_THUMBNAIL_MAP_PATHS_SIZE = 2000;
    private static final int MAX_SNAPSHOT_MAP_PATHS_SIZE = 4000;
    private static final int MAX_SUBTITLE_MAP_PATHS_SIZE = 300;

    public String getVideoPath(long videoId) {
        return videoPaths.computeIfAbsent(videoId, (key) ->
                {
                    String path = videoRepository.findById(videoId)
                            .orElseThrow(() -> new ResourceNotFoundException("Video with given id does not exist."))
                            .getPath();
                    if(videoPaths.size() > MAX_VIDEO_MAP_PATHS_SIZE) videoPaths.clear();
                    return path;
                }
        );
    }

    public String getThumbnailPath(long mediaId) {
        return thumbnailPaths.computeIfAbsent(mediaId, (key) ->
                {
                    String path = mediaRepository.findById(mediaId)
                            .orElseThrow(() -> new ResourceNotFoundException("Thumbnail with given id does not exist."))
                            .getThumbnail();
                    if(thumbnailPaths.size() > MAX_THUMBNAIL_MAP_PATHS_SIZE) thumbnailPaths.clear();
                    return path;
                }
        );
    }

    public String getSnapshotPath(long videoId) {
        return snapshotPaths.computeIfAbsent(videoId, (key) ->
                {
                    String path = videoRepository.findById(videoId)
                            .orElseThrow(() -> new ResourceNotFoundException("Snapshot with given id does not exist."))
                            .getSnapshot();
                    if(snapshotPaths.size() > MAX_SNAPSHOT_MAP_PATHS_SIZE) snapshotPaths.clear();
                    return path;
                }
        );
    }

    public String getSubtitlePath(long videoId) {
        return subtitlePaths.computeIfAbsent(videoId, (key) ->
                {
                    String path = subtitleRepository.findById(videoId)
                            .orElseThrow(() -> new ResourceNotFoundException("Subtitle with given id does not exist."))
                            .getPath();
                    if(subtitlePaths.size() > MAX_SUBTITLE_MAP_PATHS_SIZE) subtitlePaths.clear();
                    return path;
                }
        );
    }
}
