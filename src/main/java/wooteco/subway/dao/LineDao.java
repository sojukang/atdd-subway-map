package wooteco.subway.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import wooteco.subway.domain.Line;

public class LineDao {

    private static final LineDao INSTANCE = new LineDao();
    private final List<Line> lines = new ArrayList<>();
    private long seq = 0L;

    private LineDao() {
    }

    public static LineDao getInstance() {
        return INSTANCE;
    }

    public Line save(Line line) {
        Line persistStation = createNewObject(line);
        lines.add(persistStation);
        return persistStation;
    }

    private Line createNewObject(Line line) {
        return new Line(++seq, line.getName(), line.getColor());
    }

    public List<Line> findAll() {
        return List.copyOf(lines);
    }

    public Optional<Line> findById(Long id) {
        return lines.stream().filter(line -> line.getId().equals(id)).findFirst();
    }

    public void update(Long id, String name, String color) {
        int idx = 0;
        for (Line line : lines) {
            if (line.getId().equals(id)) {
                lines.set(idx, new Line(id, name, color));
                return;
            }
            idx++;
        }
    }

    public void deleteById(Long id) {
        if (!lines.removeIf(line -> line.getId().equals(id))) {
            throw new IllegalArgumentException("존재하지 않는 노선 입니다.");
        }
    }
}
