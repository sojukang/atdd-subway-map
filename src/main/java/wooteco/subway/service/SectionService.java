package wooteco.subway.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.SectionRepository;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.SectionDto;

@Service
public class SectionService {

    private final StationService stationService;
    private final SectionRepository sectionRepository;

    public SectionService(StationService stationService, SectionRepository sectionRepository) {
        this.stationService = stationService;
        this.sectionRepository = sectionRepository;
    }

    public void createSection(SectionDto sectionDto) {
        sectionRepository.save(sectionDto);
    }

    @Transactional
    public void addSectionInLine(SectionDto sectionDto) {
        Section newSection = sectionRepository.getSection(sectionDto);
        Sections sections = sectionRepository.getSectionsByLineId(sectionDto.getLineId());
        Section sectionToUpdate = sections.add(newSection);
        updateModifiedSections(sectionDto.getLineId(), List.of(sectionToUpdate));
        sectionRepository.save(sectionDto);
    }

    private Station getStationById(Long stationId) {
        return stationService.findById(stationId);
    }

    private void updateModifiedSections(Long lineId, List<Section> modifiedSections) {
        for (Section section : modifiedSections) {
            sectionRepository.update(lineId, section);
        }
    }

    public List<Station> findStationsByLineId(Long lineId) {
        return sectionRepository.getSectionsByLineId(lineId).getStations();
    }

    @Transactional
    public void deleteSectionByStationId(Long lineId, Long stationId) {
        Sections sections = sectionRepository.getSectionsByLineId(lineId);
        List<Section> originalSections = sections.getValues();

        sectionRepository.deleteById(sections.delete(getStationById(stationId)).getId());
        updateModifiedSections(lineId, sections.getDifference(originalSections));
    }
}
