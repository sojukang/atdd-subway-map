package wooteco.subway.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Line {

    public static final String NAME_REQUIRE_NOT_NULL = "이름은 Null 일 수 없습니다.";
    public static final String COLOR_REQUIRE_NOT_NULL = "색상은 Null 일 수 없습니다.";
    public static final String NAME_RANGE_VALIDATION = "이름은 1~30 자 이내여야 합니다.";
    public static final String COLOR_RANGE_VALIDATION = "색상은 1~20 자 이내여야 합니다.";

    private static final int MIN_NAME_LENGTH = 1;
    private static final int MIN_COLOR_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 30;
    private static final int MAX_COLOR_LENGTH = 20;

    private final Long id;
    private final String name;
    private final String color;
    private final List<Station> stations;

    public Line(Long id, String name, String color, List<Station> stations) {
        this.stations = List.copyOf(stations);
        validateName(name);
        validateColor(color);
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, Collections.emptyList());
    }

    public Line(String name, String color) {
        this(null, name, color);
    }

    public Line(Line line, List<Station> stations) {
        this(line.getId(), line.getName(), line.getColor(), stations);
    }

    private void validateName(String name) {
        Objects.requireNonNull(name, NAME_REQUIRE_NOT_NULL);
        validateNameLength(name);
    }

    private void validateColor(String color) {
        Objects.requireNonNull(color, COLOR_REQUIRE_NOT_NULL);
        validateColorLength(color);
    }

    private void validateNameLength(String name) {
        int length = name.length();
        if (length < MIN_NAME_LENGTH || length > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(NAME_RANGE_VALIDATION);
        }
    }

    private void validateColorLength(String color) {
        int length = color.length();
        if (length < MIN_COLOR_LENGTH || length > MAX_COLOR_LENGTH) {
            throw new IllegalArgumentException(COLOR_RANGE_VALIDATION);
        }
    }

    public boolean hasSameId(Line line) {
        return id.equals(line.getId());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        return List.copyOf(stations);
    }
}
