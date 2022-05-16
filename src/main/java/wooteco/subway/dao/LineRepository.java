package wooteco.subway.dao;

import java.util.List;
import java.util.Optional;

import wooteco.subway.domain.Line;
import wooteco.subway.service.dto.LineDto;
import wooteco.subway.service.dto.LineUpdateDto;

public interface LineRepository {

    int deleteById(Long id);

    void update(LineUpdateDto lineUpdateDto);

    Optional<Line> findByName(String name);

    Line findById(Long id);

    List<Line> findAll();

    Line save(LineDto lineDto);
}
