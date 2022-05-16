package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

import wooteco.subway.dao.entity.LineEntity;
import wooteco.subway.dao.entity.SectionEntity;
import wooteco.subway.service.dto.StationDto;

@JdbcTest
class JdbcSectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SectionDao sectionDao;
    private StationDao stationDao;
    private Long savedLineId;
    private Long stationIdA;
    private Long stationIdB;

    @BeforeEach
    void setUp() {
        sectionDao = new JdbcSectionDao(jdbcTemplate);
        LineDao lineDao = new JdbcLineDao(jdbcTemplate);
        stationDao = new JdbcStationDao(jdbcTemplate);

        stationIdA = stationDao.save(new StationDto("강남역")).getId();
        stationIdB = stationDao.save(new StationDto("선릉역")).getId();

        savedLineId = lineDao.save(new LineEntity("2호선", "green")).getId();
    }

    @Test
    @DisplayName("구간을 저장한다.")
    void save() {
        //when
        Long sectionId = sectionDao.save(new SectionEntity(savedLineId, stationIdA, stationIdB, 5));

        //then
        List<SectionEntity> actual = sectionDao.findByLineId(savedLineId);
        List<SectionEntity> expected = List.of(new SectionEntity(sectionId, savedLineId, stationIdA, stationIdB, 5));

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하지 않는 노선 정보로 구간을 저장시 예외를 던진다.")
    void saveWithInvalidLine() {
        assertThatThrownBy(() -> sectionDao.save(new SectionEntity(100L, stationIdA, stationIdB, 5)))
            .isInstanceOf(DataIntegrityViolationException.class)
            .hasMessageContaining("foreign key 데이터가 존재하지 않아 추가할 수 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 역 정보로 구간을 저장시 예외를 던진다.")
    void saveWithInvalidStation() {
        assertThatThrownBy(() -> sectionDao.save(new SectionEntity(savedLineId, stationIdA, 100L, 5)))
            .isInstanceOf(DataIntegrityViolationException.class)
            .hasMessageContaining("foreign key 데이터가 존재하지 않아 추가할 수 없습니다.");
    }

    @Test
    @DisplayName("노선 id 로 구간을 조회한다.")
    void findByLineId() {
        //given
        Long stationIdC = stationDao.save(new StationDto("서초역")).getId();

        Long sectionIdA = sectionDao.save(new SectionEntity(savedLineId, stationIdA, stationIdB, 5));
        Long sectionIdB = sectionDao.save(new SectionEntity(savedLineId, stationIdB, stationIdC, 5));

        //when
        List<SectionEntity> actual = sectionDao.findByLineId(savedLineId);
        List<SectionEntity> expected = List.of(new SectionEntity(sectionIdA, savedLineId, stationIdA, stationIdB, 5),
            new SectionEntity(sectionIdB, savedLineId, stationIdB, stationIdC, 5));

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("구간 정보를 수정한다.")
    void update() {
        //given
        Long stationIdC = stationDao.save(new StationDto("서초역")).getId();

        Long sectionId = sectionDao.save(new SectionEntity(savedLineId, stationIdA, stationIdB, 5));

        //when
        sectionDao.update(new SectionEntity(sectionId, savedLineId, stationIdC, stationIdB, 4));

        //then
        List<SectionEntity> actual = sectionDao.findByLineId(savedLineId);
        SectionEntity expected = new SectionEntity(sectionId, savedLineId, stationIdC, stationIdB, 4);
        assertThat(actual).contains(expected);
    }

    @Test
    @DisplayName("id 에 해당하는 구간을 삭제한다.")
    void deleteById() {
        //given
        Long sectionId = sectionDao.save(new SectionEntity(savedLineId, stationIdA, stationIdB, 5));

        //when
        sectionDao.deleteById(sectionId);

        //then
        List<SectionEntity> actual = sectionDao.findByLineId(savedLineId);
        SectionEntity expected = new SectionEntity(sectionId, savedLineId, stationIdA, stationIdB, 5);
        assertThat(actual).doesNotContain(expected);
    }
}
