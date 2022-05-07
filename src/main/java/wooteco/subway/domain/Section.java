package wooteco.subway.domain;

import java.util.Objects;

public class Section {

    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public Section(Long upStationId, Long downStationId, int distance) {
        validateIds(upStationId, downStationId);
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    private void validateIds(Long upStationId, Long downStationId) {
        Objects.requireNonNull(upStationId, "상행선 Id 가 Null 입니다.");
        Objects.requireNonNull(downStationId, "하행선 Id 가 Null 입니다.");
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }
}
