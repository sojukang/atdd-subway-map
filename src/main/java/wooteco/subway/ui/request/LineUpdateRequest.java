package wooteco.subway.ui.request;

import static wooteco.subway.domain.Line.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import wooteco.subway.dao.entity.LineEntity;

public class LineUpdateRequest {

    @NotNull(message = NAME_REQUIRE_NOT_NULL)
    @Size(min = 1, max = 30, message = NAME_RANGE_VALIDATION)
    private String name;

    @NotNull(message = COLOR_REQUIRE_NOT_NULL)
    @Size(min = 1, max = 20, message = COLOR_RANGE_VALIDATION)
    private String color;

    public LineUpdateRequest() {
    }

    public LineUpdateRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public LineEntity toLineEntity(Long id) {
        return new LineEntity(id, name, color);
    }
}
