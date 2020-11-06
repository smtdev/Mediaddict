package me.sergiomartin.tvshowmovietracker.moviesModule.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import me.sergiomartin.tvshowmovietracker.moviesModule.model.Trailer;

public class TrailerResponse {

    @SerializedName("results")
    @Expose
    private List<Trailer> trailers;

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }
}