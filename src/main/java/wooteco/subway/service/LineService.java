package wooteco.subway.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.LineRepository;
import wooteco.subway.domain.Line;
import wooteco.subway.exception.DataDuplicationException;
import wooteco.subway.exception.DataNotFoundException;
import wooteco.subway.service.dto.LineDto;
import wooteco.subway.service.dto.LineUpdateDto;
import wooteco.subway.service.dto.SectionDto;

@Service
public class LineService {

    private static final int ROW_SIZE_WHEN_NOT_DELETED = 0;

    private final LineRepository lineRepository;
    private final SectionService sectionService;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, SectionService sectionService, StationService stationService) {
        this.lineRepository = lineRepository;
        this.sectionService = sectionService;
        this.stationService = stationService;
    }

    @Transactional
    public Line createLine(LineDto lineDto) {
        Optional<Line> foundLine = lineRepository.findByName(lineDto.getName());
        if (foundLine.isPresent()) {
            throw new DataDuplicationException("이미 등록된 노선입니다.");
        }
        Line newLine = lineRepository.save(lineDto);
        sectionService.createSection(SectionDto.of(newLine.getId(), lineDto));

        return new Line(newLine,
            stationService.findBothStationsByIds(lineDto.getUpStationId(), lineDto.getDownStationId()));
    }

    public List<Line> findAll() {
        return lineRepository.findAll();
    }

    public Line findById(Long id) {
        return lineRepository.findById(id);
    }

    public void update(LineUpdateDto lineUpdateDto) {
        Optional<Line> foundLine = lineRepository.findByName(lineUpdateDto.getName());
        if (foundLine.isPresent() && !lineUpdateDto.getId().equals(foundLine.get().getId())) {
            throw new DataDuplicationException("이미 등록된 노선입니다.");
        }
        lineRepository.update(lineUpdateDto);
    }

    public void deleteById(Long id) {
        if (lineRepository.deleteById(id) == ROW_SIZE_WHEN_NOT_DELETED) {
            throw new DataNotFoundException("존재하지 않는 노선입니다.");
        }
    }
}
