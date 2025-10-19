package nl.nielsvanbruggen.videostreamingplatform.video.service;

import com.sun.jdi.InternalException;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import nl.nielsvanbruggen.videostreamingplatform.config.PathProperties;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.video.exception.VideoException;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Subtitle;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import nl.nielsvanbruggen.videostreamingplatform.video.repository.SubtitleRepository;
import nl.nielsvanbruggen.videostreamingplatform.video.repository.VideoRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.String.format;

@Service
public class VideoService {
    private static final Pattern subtitlePattern = Pattern.compile("(.+)_([a-z]{2})_(\\p{Alnum}+)(.\\p{Alpha}+)");
    private final VideoRepository videoRepository;
    private final SubtitleRepository subtitleRepository;
    private final PathProperties pathProperties;
    private final FFmpeg ffmpeg;
    private final FFprobe ffprobe;

    public VideoService(VideoRepository videoRepository, SubtitleRepository subtitleRepository, PathProperties pathProperties) throws IOException {
        this.videoRepository = videoRepository;
        this.subtitleRepository = subtitleRepository;
        this.pathProperties = pathProperties;
        this.ffmpeg = new FFmpeg(pathProperties.getFfmpeg().getRoot());
        this.ffprobe = new FFprobe(pathProperties.getFfprobe().getRoot());
    }

    public Video getVideo(long videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoException(format("Video with id: %d does not exist", videoId)));
    }

    public void updateVideos(Media media) throws VideoException {
        File mediaDir;

        try (Stream<Path> pathStream = Files.walk(Paths.get(pathProperties.getVideos().getRoot()), 2, FileVisitOption.FOLLOW_LINKS)) {
            mediaDir = pathStream
                    .filter(file -> file.getFileName().toString().equals(media.getName()))
                    .findFirst()
                    .orElseThrow(() -> new VideoException("No folder on system associated with media: " + media.getName()))
                    .toFile();
        } catch (IOException e) {
            throw new VideoException(e);
        }

        if(!mediaDir.isDirectory()) {
            throw new VideoException("Object found on disk is not a folder");
        }

        List<Path> videos = new LinkedList<>();
        List<Path> subtitles = new LinkedList<>();

        // Find all videos and subtitles of media on disk.
        try (Stream<Path> pathStream = Files.walk(mediaDir.toPath(), MAX_VALUE, FileVisitOption.FOLLOW_LINKS)) {
            pathStream
                    .forEach(file -> {
                        if (FilenameUtils.getExtension(file.getFileName().toString()).equals("mp4")) {
                            videos.add(file);
                        }
                        if (FilenameUtils.getExtension(file.getFileName().toString()).equals("vtt")) {
                            subtitles.add(file);
                        }
                    });

            Collections.sort(videos);
        } catch (IOException e) {
            throw new VideoException(format("Media: %s", media.getName()), e);
        }

        List<String> parsedVideos = videos.stream()
                .map(this::parsePath)
                .toList();
        List<Video> dbVideos = videoRepository.findAllByMedia(media);
        // Deletes all videos from db that don't exist in the folder.
        dbVideos.forEach(video -> {
            if (!parsedVideos.contains(video.getPath())) {
                videoRepository.delete(video);
            }
        });

        int newEntries = 0;
        for (Path videoPath : videos) {
            int seasonIndex = videoPath.getParent().toString().lastIndexOf("Season");
            int season = seasonIndex == -1 ? -1 : Integer.parseInt(videoPath.getParent().toString().substring(seasonIndex + 6).trim());

            Video video = videoRepository.findByPath(parsePath(videoPath))
                    .orElseGet(Video::new);

            if (video.getId() == 0) {
                video.setIndex(dbVideos.size() + newEntries);
                newEntries++;
            }

            video.setName(videoPath.getFileName().toString().replace(".mp4", ""));
            video.setPath(parsePath(videoPath));
            video.setMedia(media);
            video.setSeason(season);
            videoRepository.save(video);

            createSnapshot(video, videoPath);
            persistSubtitles(video, videoPath, subtitles);
        }
    }

    private void createSnapshot(Video video, Path videoPath) {
        try {
            FFmpegProbeResult probeResult = ffprobe.probe(videoPath.toString());
            float duration = (float) probeResult.getFormat().duration;
            // This assumes the first stream is the video stream.
            FFmpegStream videoStream = probeResult.getStreams().getFirst();

            video.setDuration(duration);
            video.setXResolution(videoStream.width);
            video.setYResolution(videoStream.height);

            int screenshotAtTime = (int) duration / 10;
            Path snapshotPath = Path.of(pathProperties.getSnapshot().getRoot(), video.getName() + ".jpg");

            ffmpeg.run(new FFmpegBuilder()
                    .setStartOffset(screenshotAtTime, TimeUnit.SECONDS)
                    .setInput(videoPath.toString())
                    .addOutput(snapshotPath.toString())
                    .setFrames(1)
                    .setVideoFilter("scale=1000:-1")
                    .setVideoCodec("mjpeg")
                    .setVideoQuality(5)
                    .done()
            );
            video.setSnapshot(video.getName() + ".jpg");

            videoRepository.save(video);
        } catch (IOException ex) {
            throw new InternalException(ex.getMessage());
        }
    }

    private void persistSubtitles(Video video, Path videoPath, List<Path> subtitles) {
        List<Subtitle> subs = subtitles.stream()
                .map(subtitle -> subtitlePattern.matcher(subtitle.toString()))
                .filter(Matcher::find)
                .filter(matcher -> matcher.group(1).equals(videoPath.toString().replace(".mp4", "")))
                .map(matcher -> Subtitle.builder()
                        .defaultSub(matcher.group(2).equals("en"))
                        .srcLang(matcher.group(2))
                        .label(matcher.group(3))
                        .path(parsePath(Path.of(matcher.group(0))))
                        .video(video)
                        .build())
                .toList();

        subtitleRepository.deleteAllByVideo(video);
        subtitleRepository.saveAll(subs);
    }

    private String parsePath(Path path) {
        return path.toString()
                .replace("\\", "/")
                .replace(pathProperties.getVideos().getRoot(), "");
    }
}
