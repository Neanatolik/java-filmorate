package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

@Data
public class Film {
    private Long id;
    @NonNull
    @NotBlank
    private String name;
    @NotBlank
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @Positive
    @NonNull
    private int duration;
    private Integer rate;
    private LinkedHashSet<Genres> genres;
    @NonNull
    private Mpa mpa;

    public Film(Long id, String name, String description, LocalDate releaseDate, int duration, Integer rate, Mpa mpa, LinkedHashSet<Genres> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
        this.genres = genres;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("rate", rate);
        values.put("mpa", mpa.getId());
        values.put("genres", genres);
        return values;
    }

}
