package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StationsTest {

    @Test
    @DisplayName("상행 종점 등록을 검증한다.")
    void addUpDestination() {
        //given
        Section oldSection = new Section(1L, 2L, 5);
        Stations stations = new Stations(oldSection);

        //when
        Section newSection = new Section(3L, 1L, 5);
        stations.add(newSection);

        //then
        assertThat(stations.isUpStationId(3L)).isTrue();
    }
}
