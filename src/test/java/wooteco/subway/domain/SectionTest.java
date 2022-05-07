package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionTest {

    @Test
    @DisplayName("생성시 상행, 하행 중 하나의 id 라도 없으면 예외를 던진다.")
    void newSection() {
        //when, then
        assertThatThrownBy(() -> {
            new Section(1L, null, 5);
            new Section(null, 2L, 5);
        }).isInstanceOf(NullPointerException.class);
    }
}
