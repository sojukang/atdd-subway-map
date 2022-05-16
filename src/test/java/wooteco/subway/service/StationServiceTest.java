package wooteco.subway.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.Station;
import wooteco.subway.exception.DataDuplicationException;
import wooteco.subway.exception.DataNotFoundException;
import wooteco.subway.service.dto.StationDto;

class StationServiceTest {

    private StationService stationService;

    @BeforeEach
    void setUp() {
        stationService = new StationService(new FakeStationRepository(new FakeStationDao()));
    }

    @Test
    @DisplayName("station 을 저장한다.")
    void create() {
        //given
        StationDto station = new StationDto("lala");

        //when
        Station actual = stationService.createStation(station);

        //then
        assertThat(actual.getName()).isEqualTo(station.getName());
    }

    @Test
    @DisplayName("중복된 역을 저장할 수 없다.")
    void createDuplicateName() {
        //given
        StationDto station = new StationDto("lala");
        stationService.createStation(station);

        //then
        assertThatThrownBy(() -> stationService.createStation(station))
            .isInstanceOf(DataDuplicationException.class)
            .hasMessageContaining("이미 등록된 역입니다.");
    }

    @Test
    @DisplayName("모든 역 목록을 조회한다.")
    void findAll() {
        //given
        StationDto station1 = new StationDto("lala");
        StationDto station2 = new StationDto("sojukang");
        stationService.createStation(station1);
        stationService.createStation(station2);

        //when
        List<Station> actual = stationService.findAll();

        //then
        assertAll(
            () -> assertThat(actual.get(0).getName()).isEqualTo(station1.getName()),
            () -> assertThat(actual.get(1).getName()).isEqualTo(station2.getName())
        );
    }

    @Test
    @DisplayName("id 로 역을 조회한다.")
    void findById() {
        //given
        StationDto station = new StationDto("lala");
        Long id = stationService.createStation(station).getId();

        //when
        Station actual = stationService.findById(id);

        //then
        assertThat(actual.getName()).isEqualTo(station.getName());
    }

    @Test
    @DisplayName("존재하지 않는 id 로 조회할 경우 예외를 던진다.")
    void findByIdNotExist() {
        //given
        StationDto station = new StationDto("lala");
        Long id = stationService.createStation(station).getId();

        //then
        assertThatThrownBy(() -> stationService.findById(id + 1))
            .isInstanceOf(DataNotFoundException.class)
            .hasMessage("존재하지 않는 역입니다.");
    }

    @Test
    @DisplayName("두 id 로 두 역을 조회한다.")
    void findBothStationsByIds() {
        //given
        StationDto stationA = new StationDto("lala");
        StationDto stationB = new StationDto("sojukang");
        Long idA = stationService.createStation(stationA).getId();
        Long idB = stationService.createStation(stationB).getId();

        //when
        List<Station> actual = stationService.findBothStationsByIds(idA, idB);

        //then
        assertAll(
            () -> assertThat(actual.get(0).getName()).isEqualTo(stationA.getName()),
            () -> assertThat(actual.get(1).getName()).isEqualTo(stationB.getName())
        );
    }

    @Test
    @DisplayName("역을 삭제한다.")
    void deleteById() {
        //given
        StationDto station = new StationDto("이수");
        Station createdStation = stationService.createStation(station);
        stationService.deleteById(createdStation.getId());

        //then
        assertThatThrownBy(() -> stationService.findById(createdStation.getId()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 역입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 id 로 삭제할 경우 예외를 던진다.")
    void deleteByIdWithIdNotExists() {
        //then
        assertThatThrownBy(() -> stationService.deleteById(1L))
            .isInstanceOf(DataNotFoundException.class)
            .hasMessageContaining("존재하지 않는 역입니다.");
    }
}
