package wooteco.subway.dao;

import java.util.List;
import java.util.Optional;

import wooteco.subway.domain.Station;

public interface StationRepository {

    Station findById(Long id);

    Optional<Station> findByName(String name);

    Station save(Station station);

    List<Station> findStationsByIds(Long idA, Long idB);

    List<Station> findAll();

    int deleteById(Long id);
}
