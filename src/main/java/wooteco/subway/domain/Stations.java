package wooteco.subway.domain;

import java.util.LinkedList;
import java.util.List;

public class Stations {

    final List<Long> stations = new LinkedList<>();

    public Stations(Section section) {
        this.stations.add(section.getUpStationId());
        this.stations.add(section.getDownStationId());
    }

    public boolean isUpDestinationId(Long id) {
        return getUpDestinationId().equals(id);
    }

    public boolean isDownDestinationId(Long id) {
        return getDownDestinationId().equals(id);
    }

    private Long getDownDestinationId() {
        return stations.get(stations.size() - 1);
    }

    private Long getUpDestinationId() {
        return stations.get(0);
    }

    public void add(Section section) {
        checkAddable(section);
        if (isUpDestination(section)) {
            stations.add(0, section.getUpStationId());
        }

        if (isDownDestination(section)) {
            stations.add(stations.size(), section.getDownStationId());
        }
    }

    private boolean isDownDestination(Section section) {
        return isDownDestinationId(section.getUpStationId());
    }

    private boolean isUpDestination(Section section) {
        return isUpDestinationId(section.getDownStationId());
    }

    private void checkAddable(Section section) {
        long count = stations.stream()
            .filter(id -> id.equals(section.getUpStationId()) || id.equals(section.getDownStationId()))
            .count();

        if (count < 1) {
            throw new IllegalArgumentException("두 역 중 하나라도 노선에 포함되어 있어야합니다.");
        }

        if (count == 2) {
            throw new IllegalArgumentException("두 역이 모두 노선에 포함되어 있습니다.");
        }
    }
}
