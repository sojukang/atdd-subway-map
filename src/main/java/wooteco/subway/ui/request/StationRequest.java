package wooteco.subway.ui.request;

import wooteco.subway.service.dto.StationDto;

public class StationRequest {

    private String name;

    public StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public StationDto toStationDto() {
        return new StationDto(name);
    }
}
