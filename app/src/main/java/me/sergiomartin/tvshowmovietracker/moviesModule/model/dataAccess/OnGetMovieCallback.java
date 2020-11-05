package me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess;

import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;

public interface OnGetMovieCallback {
    void onSuccess(Movie movie);
    void onError();
}
