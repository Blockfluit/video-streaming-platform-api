package nl.nielsvanbruggen.videostreamingplatform.global.service;

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
    private final VideoRepository videoRepository;
    private final SubtitleRepository subtitleRepository;
    @Value("${env.videos.root}")
    private String videosRoot;
    @Value("${env.ffprobe.path}")
    private String ffprobePath;

    public void readVideos(Media media) {
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

            FFprobe ffprobe = new FFprobe(ffprobePath);
            // TODO: maybe a bigger threadpool?
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            for (int i = 0; i < videos.size(); i++) {
                final int index = i;
                executorService.execute(() -> {
                    Path videoPath = videos.get(index);
                    float duration = 0;
                    try {
                        duration = (float) ffprobe.probe(videoPath.toString()).getFormat().duration;
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }

                    Video video = Video.builder()
                            .name(videoPath.getFileName().toString().replace(".mp4", ""))
                            .path(videoPath.toString().replace("\\", "/").replace(videosRoot, ""))
                            .duration(duration)
                            .media(media)
                            .index(index)
                            .build();
                    videoRepository.save(video);

                    List<Subtitle> subs = subtitles.stream()
                            .filter(subtitle -> subtitle.toString().split("_")[0]
                                    .equals(videoPath.toString().replace(".mp4", "")))
                            .map(subtitle -> Subtitle.builder()
                                    .defaultSub(false)
                                    .srcLang(subtitle.getFileName().toString().split("_")[1])
                                    .label(subtitle.getFileName().toString().split("_")[2].replace(".vtt", ""))
                                    .path(subtitle.toString().replace("\\", "/").replace(videosRoot, ""))
                                    .video(video)
                                    .build())
                            .collect(Collectors.toList());
                    subtitleRepository.saveAll(subs);
                });
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
