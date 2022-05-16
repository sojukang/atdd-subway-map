package wooteco.subway.dao;

import java.util.List;
import java.util.Optional;

import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.StationDto;

public interface StationRepository {

    Station findById(Long id);

    Optional<Station> findByName(String name);

    Station save(StationDto stationDto);

    List<Station> findStationsByIds(Long idA, Long idB);

    List<Station> findAll();

    int deleteById(Long id);
}
