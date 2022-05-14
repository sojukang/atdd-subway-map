package wooteco.subway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.SectionEntity;

class FakeSectionDao implements SectionDao {

    private final List<SectionEntity> sections = new ArrayList<>();
    private Long seq = 0L;

    @Override
    public Long save(SectionEntity sectionEntity) {
        sections.add(new SectionEntity(++seq, sectionEntity.getLineId(), sectionEntity.getUpStationId(),
            sectionEntity.getDownStationId(), sectionEntity.getDistance()));
        return seq;
    }

    @Override
    public List<SectionEntity> findByLineId(Long lineId) {
        return sections.stream()
            .filter(section -> section.getLineId().equals(lineId))
            .collect(Collectors.toList());
    }

    @Override
    public void update(SectionEntity sectionEntity) {
        sections.stream()
            .filter(it -> it.getId().equals(sectionEntity.getId()))
            .findFirst()
            .ifPresent(it -> {
                sections.remove(it);
                sections.add(sectionEntity);
            });
    }

    @Override
    public void deleteById(Long id) {
        sections.stream()
            .filter(it -> it.getId().equals(id))
            .findFirst()
            .ifPresent(sections::remove);
    }
}
