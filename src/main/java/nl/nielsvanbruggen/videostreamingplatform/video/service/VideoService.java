package nl.nielsvanbruggen.videostreamingplatform.video.service;

import com.sun.jdi.InternalException;
import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import nl.nielsvanbruggen.videostreamingplatform.config.PathProperties;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.video.exception.VideoException;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Subtitle;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import nl.nielsvanbruggen.videostreamingplatform.video.repository.VideoRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class VideoService {
    @Value("${ffmpeg.path}")
    private String ffMpegPath;
    @Value("${ffprobe.path}")
    private String ffProbePath;
    private static final Pattern subtitlePattern = Pattern.compile("(\\p{Alnum}+)_([a-z]{2})_(\\p{Alnum}+)");
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final VideoRepository videoRepository;
    private final SubtitleService subtitleService;
    private final PathProperties pathProperties;

    public Video getVideo(long videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoException(format("Video with id: %d does not exist", videoId)));
    }

    public void updateVideos(Media media) throws VideoException {
        File mediaDir = Path.of(pathProperties.getVideos().getRoot(), media.getName()).toFile();

        if(!mediaDir.exists() || !mediaDir.isDirectory()) {
            throw new VideoException(format("media: %s does not have files.", media.getName()));
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

            executorService.execute(() -> createSnapshot(video, videoPath));
            persistSubtitles(video, videoPath, subtitles);
        }
    }

    private void createSnapshot(Video video, Path videoPath) {
        try {
            FFprobe ffprobe = new FFprobe(ffProbePath);
            FFmpeg ffmpeg = new FFmpeg(ffMpegPath);
            float duration = (float) ffprobe.probe(videoPath.toString()).getFormat().duration;
            video.setDuration(duration);

            final int screenshotAtTime = (int) duration / 10;
            ffmpeg.run(new FFmpegBuilder()
                    .setStartOffset(screenshotAtTime, TimeUnit.SECONDS)
                    .setInput(videoPath.toString())
                    .addOutput(pathProperties.getSnapshot().getRoot() + video.getName() + ".jpg")
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
                .filter(subtitle -> subtitlePattern.matcher(subtitle.toString()).find())
                .filter(subtitle -> subtitlePattern.matcher(subtitle.toString()).group(1)
                        .equals(videoPath.toString().replace(".mp4", "")))
                .map(subtitle -> {
                        Matcher matcher = subtitlePattern.matcher(subtitle.getFileName().toString());

                        return Subtitle.builder()
                                .defaultSub(matcher.group(2).equals("en"))
                                .srcLang(matcher.group(2))
                                .label(matcher.group(3))
                                .path(parsePath(subtitle))
                                .video(video)
                                .build();
                })
                .toList();

        subtitleService.saveAll(subs);
    }

    private String parsePath(Path path) {
        return path.toString()
                .replace("\\", "/")
                .replace(pathProperties.getVideos().getRoot(), "");
    }
}
