package me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.get;

import java.util.List;

import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;

public interface OnGetMoviesCallback {
    void onSuccess(int page, List<Movie> movies);
    void onError();
}
