package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import wooteco.subway.dao.entity.LineEntity;
import wooteco.subway.dao.entity.SectionEntity;
import wooteco.subway.dao.entity.StationEntity;
import wooteco.subway.exception.DataReferenceViolationException;
import wooteco.subway.service.dto.StationDto;

@JdbcTest
class JdbcStationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private JdbcStationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new JdbcStationDao(jdbcTemplate);
    }

    @Test
    @DisplayName("Station 을 저장한다.")
    void save() {
        //given
        StationDto station = new StationDto("lala");

        //when
        StationEntity actual = stationDao.save(station);

        //then
        assertThat(actual.getName()).isEqualTo(station.getName());
    }

    @Test
    @DisplayName("모든 Station 을 조회한다.")
    void findAll() {
        //given
        StationDto station1 = new StationDto("lala");
        StationDto station2 = new StationDto("sojukang");
        stationDao.save(station1);
        stationDao.save(station2);

        //when
        List<StationEntity> actual = stationDao.findAll();

        //then
        assertAll(
            () -> assertThat(actual.get(0).getName()).isEqualTo(station1.getName()),
            () -> assertThat(actual.get(1).getName()).isEqualTo(station2.getName())
        );
    }

    @Test
    @DisplayName("이름으로 station 을 조회한다.")
    void findByName() {
        //given
        String name = "lala";
        stationDao.save(new StationDto(name));

        //when
        StationEntity actual = stationDao.findByName(name).get();

        //then
        assertThat(actual.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("존재하지 않는 station 의 이름으로 조회할 경우 빈 Optional 을 반환한다.")
    void findByNameNotExists() {
        //given
        String name = "lala";
        stationDao.save(new StationDto(name));

        //when
        Optional<StationEntity> actual = stationDao.findByName("sojukang");

        //then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("id 로 station 을 조회한다.")
    void findById() {
        //given
        StationEntity station = stationDao.save(new StationDto("lala"));

        //when
        StationEntity actual = stationDao.findById(station.getId()).get();

        //then
        assertThat(actual.getName()).isEqualTo(station.getName());
    }

    @Test
    @DisplayName("두 id 로 두 station 을 조회한다.")
    void findStationsByIds() {
        //given
        StationEntity stationA = stationDao.save(new StationDto("lala"));
        StationEntity stationB = stationDao.save(new StationDto("sojukang"));

        //when
        List<StationEntity> actual = stationDao.findStationsByIds(stationA.getId(), stationB.getId());

        //then
        assertAll(
            () -> assertThat(actual.get(0).getName()).isEqualTo(stationA.getName()),
            () -> assertThat(actual.get(1).getName()).isEqualTo(stationB.getName())
        );
    }

    @Test
    @DisplayName("id 로 station 을 삭제한다.")
    void deleteById() {
        //given
        String name = "lala";
        StationEntity savedStation = stationDao.save(new StationDto(name));

        //when
        stationDao.deleteById(savedStation.getId());

        //then
        assertThat(stationDao.findByName(name)).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 id 를 삭제하면 0을 반환한다.")
    void deleteByIdNotExists() {
        //when
        int actual = stationDao.deleteById(1L);

        //then
        assertThat(actual).isEqualTo(0);
    }

    @Test
    @DisplayName("특정 구간에 속한 역을 삭제하려 할 경우 예외를 던진다.")
    void deleteStationInSectionException() {
        //given
        Long stationIdA = stationDao.save(new StationDto("강남역")).getId();
        Long stationIdB = stationDao.save(new StationDto("선릉역")).getId();

        Long savedLineId = new JdbcLineDao(jdbcTemplate).save(new LineEntity("2호선", "green")).getId();

        Long stationIdC = stationDao.save(new StationDto("서초역")).getId();

        SectionDao sectionDao = new JdbcSectionDao(jdbcTemplate);

        sectionDao.save(new SectionEntity(savedLineId, stationIdA, stationIdB, 5));
        sectionDao.save(new SectionEntity(savedLineId, stationIdB, stationIdC, 5));

        //when, then
        assertThatThrownBy(() -> stationDao.deleteById(stationIdC))
            .isInstanceOf(DataReferenceViolationException.class)
            .hasMessageContaining("연관된 데이터가 존재하여 삭제할 수 없습니다.");
    }
}
