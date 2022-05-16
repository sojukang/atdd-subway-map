package wooteco.subway.service.dto;

import wooteco.subway.domain.Station;

public class StationDto {

    private final Long id;
    private final String name;

    public StationDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public StationDto(String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Station toStation() {
        return new Station(id, name);
    }
}
