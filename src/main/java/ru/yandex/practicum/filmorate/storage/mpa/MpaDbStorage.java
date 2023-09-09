package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getMpas() {
        String getAllMpa = "SELECT * FROM MPA_RATING";
        return jdbcTemplate.query(getAllMpa, (rs, rowNum) -> makeMpa(rs));
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name"));
    }

    @Override
    public Long amountOfMpas() {
        String getAmountOfMpa = "SELECT COUNT(mpa_id) FROM MPA_RATING";
        return jdbcTemplate.queryForObject(getAmountOfMpa, Long.class);
    }

    @Override
    public Mpa getMpa(int id) {
        String getMpa = "SELECT * FROM MPA_RATING WHERE mpa_id = ?";
        return jdbcTemplate.queryForObject(getMpa, (rs, rowNum) -> makeMpa(rs), id);
    }
}
