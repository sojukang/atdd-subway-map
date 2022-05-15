package wooteco.subway.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.DataDuplicationException;
import wooteco.subway.exception.DataNotFoundException;

@Service
public class StationService {

    private static final int ROW_SIZE_WHEN_NOT_DELETED = 0;

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station createStation(Station station) {
        Optional<Station> foundStation = stationDao.findByName(station.getName());
        if (foundStation.isPresent()) {
            throw new DataDuplicationException("이미 등록된 역입니다.");
        }
        return stationDao.save(station);
    }

    public Station findById(Long id) {
        return stationDao.findById(id)
            .orElseThrow(() -> new DataNotFoundException("존재하지 않는 역입니다."));
    }

    public List<Station> findBothStationsByIds(Long idA, Long idB) {
        List<Station> stations = stationDao.findStationsByIds(idA, idB);
        if (stations.size() != 2) {
            throw new DataNotFoundException("존재하지 않는 역이 포함되어 있습니다.");
        }
        return stations;
    }

    public List<Station> findAll() {
        return stationDao.findAll();
    }

    public void deleteById(Long id) {
        if (stationDao.deleteById(id) == ROW_SIZE_WHEN_NOT_DELETED) {
            throw new DataNotFoundException("존재하지 않는 역입니다.");
        }
    }
}
