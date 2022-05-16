package wooteco.subway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import wooteco.subway.dao.StationDao;
import wooteco.subway.dao.entity.StationEntity;
import wooteco.subway.domain.Station;

class FakeStationDao implements StationDao {

    private final List<StationEntity> stations = new ArrayList<>();
    private Long seq = 0L;

    @Override
    public StationEntity save(Station station) {
        StationEntity persistStation = createNewObject(station);
        stations.add(persistStation);
        return persistStation;
    }

    private StationEntity createNewObject(Station station) {
        return new StationEntity(++seq, station.getName());
    }

    @Override
    public Optional<StationEntity> findByName(String name) {
        return stations.stream()
            .filter(station -> name.equals(station.getName()))
            .findFirst();
    }

    @Override
    public Optional<StationEntity> findById(Long id) {
        return stations.stream()
            .filter(station -> station.getId().equals(id))
            .findFirst();
    }

    @Override
    public List<StationEntity> findStationsByIds(Long idA, Long idB) {
        return stations.stream()
            .filter(station -> station.getId().equals(idA) || station.getId().equals(idB))
            .collect(Collectors.toList());
    }

    @Override
    public List<StationEntity> findAll() {
        return List.copyOf(stations);
    }

    @Override
    public int deleteById(Long id) {
        if (!stations.removeIf(station -> station.getId().equals(id))) {
            return 0;
        }
        return 1;
    }
}
