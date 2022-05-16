package wooteco.subway.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import wooteco.subway.dao.StationDao;
import wooteco.subway.dao.StationRepository;
import wooteco.subway.dao.entity.StationEntity;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.DataNotFoundException;
import wooteco.subway.service.dto.StationDto;

public class FakeStationRepository implements StationRepository {

    private final StationDao stationDao;

    public FakeStationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Station findById(Long id) {
        return toStation(stationDao.findById(id)
            .orElseThrow(() -> new DataNotFoundException("존재하지 않는 역입니다.")));
    }

    private Station toStation(StationEntity entity) {
        return new Station(entity.getId(), entity.getName());
    }

    @Override
    public Optional<Station> findByName(String name) {
        Optional<StationEntity> foundEntity = stationDao.findByName(name);
        if (foundEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toStation(foundEntity.get()));
    }

    @Override
    public Station save(StationDto stationDto) {
        return toStation(stationDao.save(stationDto));
    }

    @Override
    public List<Station> findStationsByIds(Long idA, Long idB) {
        return stationDao.findStationsByIds(idA, idB).stream()
            .map(this::toStation)
            .collect(Collectors.toList());
    }

    @Override
    public List<Station> findAll() {
        return stationDao.findAll().stream()
            .map(this::toStation)
            .collect(Collectors.toList());
    }

    @Override
    public int deleteById(Long id) {
        return stationDao.deleteById(id);
    }
}
