package wooteco.subway.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import wooteco.subway.dao.entity.LineEntity;
import wooteco.subway.domain.Line;
import wooteco.subway.exception.DataNotFoundException;
import wooteco.subway.service.dto.LineDto;

@Repository
public class JdbcLineRepository implements LineRepository {

    private final LineDao lineDao;

    public JdbcLineRepository(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Override
    public int deleteById(Long id) {
        return lineDao.deleteById(id);
    }

    @Override
    public void update(LineEntity lineEntity) {
        lineDao.update(lineEntity);
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
