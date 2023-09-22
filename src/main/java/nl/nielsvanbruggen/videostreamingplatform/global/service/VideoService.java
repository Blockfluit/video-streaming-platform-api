package nl.nielsvanbruggen.videostreamingplatform.global.service;

import com.sun.jdi.InternalException;
import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFprobe;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Media;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Subtitle;
import nl.nielsvanbruggen.videostreamingplatform.media.model.Video;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.SubtitleRepository;
import nl.nielsvanbruggen.videostreamingplatform.media.repository.VideoRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final static ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final VideoRepository videoRepository;
    private final SubtitleRepository subtitleRepository;
    @Value("${env.videos.root}")
    private String videosRoot;
    @Value("${env.ffprobe.path}")
    private String ffprobePath;

    public void updateVideos(Media media) {
        try {
            Path dir = Files.walk(Paths.get(videosRoot), 2)
                    .filter(file -> file.getFileName().toString().equals(media.getName()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No folder on system associated with this name"));
            List<Path> videos = new ArrayList<>();
            List<Path> subtitles = new ArrayList<>();

            Files.walk(dir, 2).forEach(file -> {
                if(FilenameUtils.getExtension(file.getFileName().toString()).equals("mp4")) {
                    videos.add(file);
                }
                if(FilenameUtils.getExtension(file.getFileName().toString()).equals("vtt")) {
                    subtitles.add(file);
                }
            });

            List<String> parsedVideos = videos.stream()
                    .map(this::parsePath)
                    .toList();
            List<Video> dbVideos = videoRepository.findAllByMedia(media);
            // Deletes all videos from db that don't exist in the folder.
            dbVideos.forEach(video -> {
                        if (!parsedVideos.contains(video.getPath())) {
                            subtitleRepository.deleteAllByVideo(video);
                            videoRepository.delete(video);
                        }
                    });

                FFprobe ffprobe = new FFprobe(ffprobePath);
                int newEntries = 0;
                for (int i = 0; i < videos.size(); i++) {
                    Path videoPath = videos.get(i);

                    int seasonIndex = videoPath.getParent().toString().lastIndexOf("Season");
                    int season = seasonIndex == -1 ? -1 : Integer.parseInt(videoPath.getParent().toString().substring(seasonIndex + 6).trim());

                    Video video = videoRepository.findByPath(parsePath(videoPath))
                            .orElseGet(Video::new);

                    if(video.getId() == 0) {
                        video.setIndex(dbVideos.size() + newEntries);
                        newEntries++;
                    }

                    video.setName(videoPath.getFileName().toString().replace(".mp4", ""));
                    video.setPath(parsePath(videoPath));
                    video.setMedia(media);
                    video.setSeason(season);
                    videoRepository.save(video);

                    executorService.execute(() -> {
                        try {
//                            String path = video.getPath();
                            float duration = (float) ffprobe.probe(videoPath.toString()).getFormat().duration;
//                            Video tempVideo = videoRepository.findByPath(path)
//                                    .orElseThrow(() -> new InternalException("Cant set duration of new video"));
                            video.setDuration(duration);
                            videoRepository.save(video);
                        } catch (IOException ex) {
                            throw new InternalException(ex.getMessage());
                        }
                    });

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
                            .collect(Collectors.toList());
                    subtitleRepository.saveAll(subs);
            }
        } catch (IOException ex) {
            throw new InternalException(ex.getMessage());
        }
    }

    private String parsePath(Path path) {
        return path.toString()
                .replace("\\", "/")
                .replace(videosRoot.replace("\\", "/"), "");
    }
}
