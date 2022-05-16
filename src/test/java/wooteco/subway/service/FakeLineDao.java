package wooteco.subway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.entity.LineEntity;
import wooteco.subway.domain.Line;
import wooteco.subway.service.dto.LineUpdateDto;

class FakeLineDao implements LineDao {

    private final List<LineEntity> lines = new ArrayList<>();
    private Long seq = 0L;

    @Override
    public LineEntity save(LineEntity lineEntity) {
        LineEntity persistStation = createNewObject(lineEntity);
        lines.add(persistStation);
        return persistStation;
    }

    private LineEntity createNewObject(LineEntity lineEntity) {
        return new LineEntity(++seq, lineEntity.getName(), lineEntity.getColor());
    }

    @Override
    public Optional<LineEntity> findById(Long id) {
        return lines.stream()
            .filter(line -> line.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<LineEntity> findByName(String name) {
        return lines.stream()
            .filter(line -> name.equals(line.getName()))
            .findFirst();

    }

    @Override
    public List<LineEntity> findAll() {
        return lines;
    }

    @Override
    public void update(LineUpdateDto otherLineEntity) {
        int idx = 0;
        for (LineEntity entity : lines) {
            Line line = new Line(entity.getId(), entity.getName(), entity.getColor());
            if (line.hasSameId(
                new Line(otherLineEntity.getId(), otherLineEntity.getName(), otherLineEntity.getColor()))) {
                lines.set(idx,
                    new LineEntity(otherLineEntity.getId(), otherLineEntity.getName(), otherLineEntity.getColor()));
                return;
            }
            idx++;
        }
    }

    @Override
    public int deleteById(Long id) {
        if (!lines.removeIf(line -> line.getId().equals(id))) {
            return 0;
        }
        return 1;
    }
}
