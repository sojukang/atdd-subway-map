package wooteco.subway.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import wooteco.subway.dao.entity.StationEntity;
import wooteco.subway.exception.DataReferenceViolationException;
import wooteco.subway.service.dto.StationDto;

@Repository
public class JdbcStationDao implements StationDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcStationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public StationEntity save(StationDto stationDto) {
        String sql = "insert into station (name) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
            ps.setString(1, stationDto.getName());
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new StationEntity(id, stationDto.getName());
    }

    @Override
    public Optional<StationEntity> findByName(String name) {
        String sql = "select * from station where name = ?";

        try {
            StationEntity stationEntity = jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> createEntity(rs), name);
            return Optional.ofNullable(stationEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private StationEntity createEntity(ResultSet rs) throws SQLException {
        return new StationEntity(
            rs.getLong("id"),
            rs.getString("name")
        );
    }

    @Override
    public Optional<StationEntity> findById(Long id) {
        String sql = "select * from station where id = ?";

        try {
            StationEntity stationEntity = jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> createEntity(rs), id);
            return Optional.ofNullable(stationEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<StationEntity> findStationsByIds(Long idA, Long idB) {
        String sql = "select * from station where id = ? or id = ?";
        return jdbcTemplate.query(sql,
            (rs, rowNum) -> createEntity(rs), idA, idB);
    }

    @Override
    public List<StationEntity> findAll() {
        String sql = "select * from station";
        return jdbcTemplate.query(sql, (rs, rowNum) -> createEntity(rs));
    }

    @Override
    public int deleteById(Long id) {
        String sql = "delete from station where id = ?";

        try {
            return jdbcTemplate.update(sql, id);
        } catch (DataIntegrityViolationException e) {
            throw new DataReferenceViolationException("연관된 데이터가 존재하여 삭제할 수 없습니다.");
        }
    }
}
