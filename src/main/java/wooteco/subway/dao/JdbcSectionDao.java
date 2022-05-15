package wooteco.subway.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import wooteco.subway.domain.SectionEntity;
import wooteco.subway.exception.DataReferenceViolationException;

@Repository
public class JdbcSectionDao implements SectionDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcSectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(SectionEntity sectionEntity) {
        try {
            String sql = "insert into section (line_id, up_station_id, down_station_id, distance) values (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
                ps.setLong(1, sectionEntity.getLineId());
                ps.setLong(2, sectionEntity.getUpStationId());
                ps.setLong(3, sectionEntity.getDownStationId());
                ps.setInt(4, sectionEntity.getDistance());
                return ps;
            }, keyHolder);

            return Objects.requireNonNull(keyHolder.getKey()).longValue();
        } catch (DataIntegrityViolationException e) {
            throw new DataReferenceViolationException("foreign key 데이터가 존재하지 않아 추가할 수 없습니다.");
        }
    }

    @Override
    public List<SectionEntity> findByLineId(Long lineId) {
        String sql = "select * from section where line_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> createSection(rs), lineId);
    }

    @Override
    public void update(SectionEntity sectionEntity) {
        String sql = "update section set up_station_id = ?, down_station_id = ?, distance = ? where id = ?";
        jdbcTemplate.update(sql, sectionEntity.getUpStationId(), sectionEntity.getDownStationId(),
            sectionEntity.getDistance(), sectionEntity.getId());
    }

    private SectionEntity createSection(ResultSet rs) throws SQLException {
        return new SectionEntity(
            rs.getLong("id"),
            rs.getLong("line_id"),
            rs.getLong("up_station_id"),
            rs.getLong("down_station_id"),
            rs.getInt("distance")
        );
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from section where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
