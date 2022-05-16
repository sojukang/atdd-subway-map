package wooteco.subway.dao;

import java.util.List;

import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.service.dto.SectionDto;

public interface SectionRepository {

    void save(SectionDto sectionDto);

    List<Section> findByLineId(Long lineId);

    void update(Long lineId, Section section);

    void deleteById(Long id);

    Section getSection(SectionDto sectionDto);

    Sections getSectionsByLineId(Long lineId);
}
