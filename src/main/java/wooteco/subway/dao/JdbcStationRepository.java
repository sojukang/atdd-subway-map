package wooteco.subway.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import wooteco.subway.dao.entity.StationEntity;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.DataNotFoundException;
import wooteco.subway.service.dto.StationDto;

@Repository
public class JdbcStationRepository implements StationRepository {

    private final StationDao stationDao;

    public JdbcStationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Station findById(Long id) {
        return toStation(stationDao.findById(id)
            .orElseThrow(() -> new DataNotFoundException("존재하지 않는 역입니다.")));
    }

    @Override
    public Optional<Station> findByName(String name) {
        Optional<StationEntity> foundStationEntity = stationDao.findByName(name);
        if (foundStationEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toStation(foundStationEntity.get()));
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

    private Station toStation(StationEntity entity) {
        return new Station(entity.getId(), entity.getName());
    }
}
