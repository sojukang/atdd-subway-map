package wooteco.subway.dao;

import java.util.List;
import java.util.Optional;

import wooteco.subway.dao.entity.LineEntity;
import wooteco.subway.domain.Line;
import wooteco.subway.service.dto.LineDto;

public interface LineRepository {

    int deleteById(Long id);

    void update(LineEntity lineEntity);

    Optional<Line> findByName(String name);

    Line findById(Long id);

    List<Line> findAll();

    Line save(LineDto lineDto);
}
