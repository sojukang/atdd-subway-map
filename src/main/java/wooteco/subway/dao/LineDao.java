package wooteco.subway.dao;

import java.util.List;
import java.util.Optional;

import wooteco.subway.dao.entity.LineEntity;

public interface LineDao {

    LineEntity save(LineEntity lineEntity);

    Optional<LineEntity> findById(Long id);

    Optional<LineEntity> findByName(String name);

    List<LineEntity> findAll();

    void update(LineEntity lineEntity);

    int deleteById(Long id);
}
