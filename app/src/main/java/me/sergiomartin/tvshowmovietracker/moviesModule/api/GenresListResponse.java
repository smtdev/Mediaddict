package me.sergiomartin.tvshowmovietracker.moviesModule.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import me.sergiomartin.tvshowmovietracker.moviesModule.model.Genre;

public class GenresListResponse {
    @SerializedName("genres")
    @Expose
    private List<Genre> genres;

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
