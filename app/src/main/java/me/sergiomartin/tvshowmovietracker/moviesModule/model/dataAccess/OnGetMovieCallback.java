package me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess;

import java.text.ParseException;

import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;

public interface OnGetMovieCallback {
    void onSuccess(Movie movie) throws ParseException;
    void onError();
}
