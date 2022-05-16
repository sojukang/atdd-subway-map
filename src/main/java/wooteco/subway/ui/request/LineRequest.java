package wooteco.subway.ui.request;

import static wooteco.subway.domain.Line.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import wooteco.subway.service.dto.LineDto;

public class LineRequest {

    @Min(value = 1)
    private int distance;

    @NotNull(message = NAME_REQUIRE_NOT_NULL)
    @Size(min = 1, max = 30, message = NAME_RANGE_VALIDATION)
    private String name;

    @NotNull(message = COLOR_REQUIRE_NOT_NULL)
    @Size(min = 1, max = 20, message = COLOR_RANGE_VALIDATION)
    private String color;
    private Long upStationId;
    private Long downStationId;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineDto toLineDto() {
        return new LineDto(name, color, upStationId, downStationId, distance);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
