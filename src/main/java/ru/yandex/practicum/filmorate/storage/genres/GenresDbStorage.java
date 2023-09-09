package ru.yandex.practicum.filmorate.storage.genres;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenresDbStorage implements GenresStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenresDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genres getGenre(int id) {
        String getGenre = "SELECT * FROM GENRES WHERE genre_id = ?";
        return jdbcTemplate.queryForObject(getGenre, (rs, rowNum) -> makeGenre(rs), id);
    }

    @Override
    public List<Genres> getGenres() {
        String getAllGenres = "SELECT * FROM GENRES";
        return jdbcTemplate.query(getAllGenres, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Long amountOfGenres() {
        String getAmountOfGenres = "SELECT COUNT(genre_id) FROM GENRES";
        return jdbcTemplate.queryForObject(getAmountOfGenres, Long.class);
    }

    private Genres makeGenre(ResultSet rs) throws SQLException {
        return new Genres(rs.getInt("genre_id"), rs.getString("genre_name"));
    }
}
