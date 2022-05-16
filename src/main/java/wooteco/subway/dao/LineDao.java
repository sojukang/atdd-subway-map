package wooteco.subway.dao;

import java.util.List;
import java.util.Optional;

import wooteco.subway.dao.entity.LineEntity;
import wooteco.subway.service.dto.LineUpdateDto;

public interface LineDao {

    LineEntity save(LineEntity lineEntity);

    Optional<LineEntity> findById(Long id);

    Optional<LineEntity> findByName(String name);

    List<LineEntity> findAll();

    void update(LineUpdateDto lineUpdateDto);

    int deleteById(Long id);
}
