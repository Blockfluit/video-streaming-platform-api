package nl.nielsvanbruggen.videostreamingplatform.video.service;

import com.sun.jdi.InternalException;
import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import nl.nielsvanbruggen.videostreamingplatform.config.EnvironmentProperties;
import nl.nielsvanbruggen.videostreamingplatform.global.exception.ResourceNotFoundException;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.video.exception.VideoException;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Subtitle;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Video;
import nl.nielsvanbruggen.videostreamingplatform.video.repository.SubtitleRepository;
import nl.nielsvanbruggen.videostreamingplatform.video.repository.VideoRepository;
import nl.nielsvanbruggen.videostreamingplatform.watched.repository.WatchedRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final static ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final VideoRepository videoRepository;
    private final SubtitleRepository subtitleRepository;
    private final WatchedRepository watchedRepository;
    private final EnvironmentProperties env;

    public Video getVideo(long videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoException(String.format("Video with id: %d does not exist", videoId)));
    }

    public Subtitle getSubtitle(long subtitleId) {
        return subtitleRepository.findById(subtitleId)
                .orElseThrow(() -> new ResourceNotFoundException("Subtitle with given id does not exist."));
    }

    public void updateVideos(Media media) throws IOException {
        Path dir = Files.walk(Paths.get(env.getVideos().get("root")), 2, FileVisitOption.FOLLOW_LINKS)
                .filter(file -> file.getFileName().toString().equals(media.getName()))
                .findFirst()
                .orElseThrow(() -> new IOException("No folder on system associated with this name"));
        List<Path> videos = new ArrayList<>();
        List<Path> subtitles = new ArrayList<>();

        //Finds all videos and subtitles on disk
        Files.walk(dir, 2).forEach(file -> {
            if (FilenameUtils.getExtension(file.getFileName().toString()).equals("mp4")) {
                videos.add(file);
            }
            if (FilenameUtils.getExtension(file.getFileName().toString()).equals("vtt")) {
                subtitles.add(file);
            }
        });
        Collections.sort(videos);

        List<String> parsedVideos = videos.stream()
                .map(this::parsePath)
                .toList();
        List<Video> dbVideos = videoRepository.findAllByMedia(media);
        // Deletes all videos from db that don't exist in the folder.
        dbVideos.forEach(video -> {
            if (!parsedVideos.contains(video.getPath())) {
                watchedRepository.deleteAllByVideo(video);
                subtitleRepository.deleteAllByVideo(video);
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
            FFprobe ffprobe = new FFprobe(env.getFfprobe().get("path"));
            FFmpeg ffmpeg = new FFmpeg(env.getFfmpeg().get("path"));
            float duration = (float) ffprobe.probe(videoPath.toString()).getFormat().duration;
            video.setDuration(duration);

            final int screenshotAtTime = (int) duration / 10;
            ffmpeg.run(new FFmpegBuilder()
                    .setStartOffset(screenshotAtTime, TimeUnit.SECONDS)
                    .setInput(videoPath.toString())
                    .addOutput(env.getSnapshot().get("root") + video.getName() + ".jpg")
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
                .filter(subtitle -> subtitle.toString().split("_")[0]
                        .equals(videoPath.toString().replace(".mp4", "")))
                .map(subtitle -> Subtitle.builder()
                        .defaultSub(subtitle.getFileName().toString().split("_")[1].equals("en"))
                        .srcLang(subtitle.getFileName().toString().split("_")[1])
                        .label(subtitle.getFileName().toString().split("_")[2].replace(".vtt", ""))
                        .path(parsePath(subtitle))
                        .video(video)
                        .build())
                .toList();
        subtitleRepository.saveAll(subs);
    }

    private String parsePath(Path path) {
        return path.toString()
                .replace("\\", "/")
                .replace(env.getVideos().get("root"), "/");
    }
}
