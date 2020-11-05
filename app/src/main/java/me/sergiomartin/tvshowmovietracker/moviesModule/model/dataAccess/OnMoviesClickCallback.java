package me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess;

import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;

public interface OnMoviesClickCallback {
    void onClick(Movie movie);
}
