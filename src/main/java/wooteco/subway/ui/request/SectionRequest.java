package wooteco.subway.ui.request;

import javax.validation.constraints.Min;

import wooteco.subway.domain.Section;

public class SectionRequest {

    private Long upStationId;
    private Long downStationId;

    @Min(value = 1, message = Section.DISTANCE_RANGE_VALIDATION)
    private int distance;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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
