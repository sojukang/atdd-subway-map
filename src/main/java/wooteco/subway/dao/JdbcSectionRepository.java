package wooteco.subway.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import wooteco.subway.dao.entity.SectionEntity;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.SectionDto;

@Repository
public class JdbcSectionRepository implements SectionRepository {

    private final SectionDao sectionDao;
    private final StationRepository stationRepository;

    public JdbcSectionRepository(SectionDao sectionDao, StationRepository stationRepository) {
        this.sectionDao = sectionDao;
        this.stationRepository = stationRepository;
    }

    @Override
    public void save(SectionDto sectionDto) {
        sectionDao.save(sectionDto.toEntity());
    }

    @Override
    public List<Section> findByLineId(Long lineId) {
        return convertSectionEntitiesToSections(sectionDao.findByLineId(lineId));
    }

    private List<Section> convertSectionEntitiesToSections(List<SectionEntity> sectionEntities) {
        return sectionEntities.stream()
            .map(sectionEntity -> {
                Station upDestination = stationRepository.findById(sectionEntity.getUpStationId());
                Station downDestination = stationRepository.findById(sectionEntity.getDownStationId());
                return new Section(sectionEntity.getId(), upDestination, downDestination, sectionEntity.getDistance());
            })
            .collect(Collectors.toList());
    }

    @Override
    public void update(Long lineId, Section section) {
        sectionDao.update(new SectionEntity(section.getId(), lineId, section.getUpStation().getId(),
            section.getDownStation().getId(), section.getDistance()));
    }

    @Override
    public void deleteById(Long id) {
        sectionDao.deleteById(id);
    }

    @Override
    public Section getSection(SectionDto sectionDto) {
        return new Section(stationRepository.findById(sectionDto.getUpStationId()),
            stationRepository.findById(sectionDto.getDownStationId()), sectionDto.getDistance());
    }

    @Override
    public Sections getSectionsByLineId(Long lineId) {
        return new Sections(findByLineId(lineId));
    }
}
