package me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.action;

import java.util.List;

import me.sergiomartin.tvshowmovietracker.moviesModule.model.Movie;

public interface OnSearchMovieCallback {
    void onSuccess(int page, List<Movie> movies, String query);
    void onError();
    void onEmptyResult();

}
