package wooteco.subway.dao;

import java.util.List;
import java.util.Optional;

import wooteco.subway.dao.entity.StationEntity;
import wooteco.subway.service.dto.StationDto;

public interface StationDao {

    StationEntity save(StationDto stationDto);

    Optional<StationEntity> findByName(String name);

    Optional<StationEntity> findById(Long id);

    List<StationEntity> findStationsByIds(Long idA, Long idB);

    List<StationEntity> findAll();

    int deleteById(Long id);

}
