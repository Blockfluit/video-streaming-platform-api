package nl.nielsvanbruggen.videostreamingplatform.video.service;

import lombok.RequiredArgsConstructor;
import nl.nielsvanbruggen.videostreamingplatform.exception.ResourceNotFoundException;
import nl.nielsvanbruggen.videostreamingplatform.video.model.Subtitle;
import nl.nielsvanbruggen.videostreamingplatform.video.repository.SubtitleRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubtitleService {
    private final SubtitleRepository subtitleRepository;

    public Subtitle getSubtitle(long subtitleId) {
        return subtitleRepository.findById(subtitleId)
                .orElseThrow(() -> new ResourceNotFoundException("Subtitle with given id does not exist."));
    }

    public List<Subtitle> saveAll(Collection<Subtitle> subtitles) {
        return subtitleRepository.saveAll(subtitles);
    }
}
