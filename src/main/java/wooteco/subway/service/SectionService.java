package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.SectionEntity;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.SectionDto;

@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final StationService stationService;

    public SectionService(SectionDao sectionDao, StationService stationService) {
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }

    public void createSection(SectionDto sectionDto) {
        sectionDao.save(sectionDto.toEntity());
    }

    @Transactional
    public void addSectionInLine(SectionDto sectionDto) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(sectionDto.getLineId());
        Section sectionToUpdate = getSectionToUpdate(sectionDto, convertSectionEntitiesToSections(sectionEntities));
        updateModifiedSections(sectionDto.getLineId(), List.of(sectionToUpdate));
        sectionDao.save(sectionDto.toEntity());
    }

    private Section getSectionToUpdate(SectionDto sectionDto, List<Section> convertedSections) {
        Sections sections = new Sections(convertedSections);
        Section newSection = new Section(getStationById(sectionDto.getUpStationId()),
            getStationById(sectionDto.getDownStationId()), sectionDto.getDistance());
        return sections.add(newSection);
    }

    private List<Section> convertSectionEntitiesToSections(List<SectionEntity> sectionEntities) {
        return sectionEntities.stream()
            .map(sectionEntity -> {
                Station upDestination = getStationById(sectionEntity.getUpStationId());
                Station downDestination = getStationById(sectionEntity.getDownStationId());
                return new Section(sectionEntity.getId(), upDestination, downDestination, sectionEntity.getDistance());
            })
            .collect(Collectors.toList());
    }

    private Station getStationById(Long stationId) {
        return stationService.findById(stationId);
    }

    private void updateModifiedSections(Long lineId, List<Section> modifiedSections) {
        for (Section section : modifiedSections) {
            sectionDao.update(new SectionEntity(section.getId(), lineId, section.getUpStation().getId(),
                section.getDownStation().getId(), section.getDistance()));
        }
    }

    public List<Station> findStationsByLineId(Long lineId) {
        Sections sections = new Sections(convertSectionEntitiesToSections(sectionDao.findByLineId(lineId)));
        return sections.getStations();
    }

    @Transactional
    public void deleteSectionByStationId(Long lineId, Long stationId) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);
        List<Section> originalSections = convertSectionEntitiesToSections(sectionEntities);
        Sections sections = new Sections(originalSections);

        sectionDao.deleteById(sections.delete(getStationById(stationId)).getId());
        updateModifiedSections(lineId, sections.getDifference(originalSections));
    }
}
