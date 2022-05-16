package wooteco.subway.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import wooteco.subway.dao.StationRepository;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.DataDuplicationException;
import wooteco.subway.exception.DataNotFoundException;

@Service
public class StationService {

    private static final int ROW_SIZE_WHEN_NOT_DELETED = 0;

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station createStation(Station station) {
        Optional<Station> foundStation = stationRepository.findByName(station.getName());
        if (foundStation.isPresent()) {
            throw new DataDuplicationException("이미 등록된 역입니다.");
        }
        return stationRepository.save(station);
    }

    public Station findById(Long id) {
        return stationRepository.findById(id);
    }

    public List<Station> findBothStationsByIds(Long idA, Long idB) {
        List<Station> stations = stationRepository.findStationsByIds(idA, idB);
        if (stations.size() != 2) {
            throw new DataNotFoundException("존재하지 않는 역이 포함되어 있습니다.");
        }
        return stations;
    }

    public List<Station> findAll() {
        return stationRepository.findAll();
    }

    public void deleteById(Long id) {
        if (stationRepository.deleteById(id) == ROW_SIZE_WHEN_NOT_DELETED) {
            throw new DataNotFoundException("존재하지 않는 역입니다.");
        }
    }
}
