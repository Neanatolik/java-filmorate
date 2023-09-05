package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
    private List<Genre> genres;
    @NonNull
    private Mpa mpa;
    @JsonIgnore
    private Set<Long> likes;

    public Film(String name, String description, LocalDate releaseDate, int duration, Mpa mpa, List<Genre> genres, Set<Long> likes) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
        this.likes = likes;
    }

    public void addLike(Long id) {
        likes.add(id);
    }

    public void deleteLike(Long id) {
        likes.remove(id);
    }

    public int getAmountOfLikes() {
        return likes.size();
    }
}
