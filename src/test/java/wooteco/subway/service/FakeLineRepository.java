package wooteco.subway.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.LineRepository;
import wooteco.subway.dao.entity.LineEntity;
import wooteco.subway.domain.Line;
import wooteco.subway.exception.DataNotFoundException;
import wooteco.subway.service.dto.LineDto;
import wooteco.subway.service.dto.LineUpdateDto;

public class FakeLineRepository implements LineRepository {

    private final LineDao lineDao;

    public FakeLineRepository(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Override
    public int deleteById(Long id) {
        return lineDao.deleteById(id);
    }

    @Override
    public void update(LineUpdateDto lineUpdateDto) {
        lineDao.update(lineUpdateDto);
    }

    @Override
    public Optional<Line> findByName(String name) {
        Optional<LineEntity> foundLineEntity = lineDao.findByName(name);
        if (foundLineEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toLine(foundLineEntity.get()));
    }

    @Override
    public Line findById(Long id) {
        return toLine(lineDao.findById(id)
            .orElseThrow(() -> new DataNotFoundException("존재하지 않는 노선입니다.")));
    }

    @Override
    public List<Line> findAll() {
        return lineDao.findAll().stream()
            .map(this::toLine)
            .collect(Collectors.toList());
    }

    @Override
    public Line save(LineDto lineDto) {
        return toLine(lineDao.save(lineDto.toLineEntity()));
    }

    private Line toLine(LineEntity lineEntity) {
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
    }
}
