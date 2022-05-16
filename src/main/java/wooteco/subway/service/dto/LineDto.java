package wooteco.subway.service.dto;

import wooteco.subway.dao.entity.LineEntity;

public class LineDto {

    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public LineDto(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineEntity toLineEntity() {
        return new LineEntity(name, color);
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

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
