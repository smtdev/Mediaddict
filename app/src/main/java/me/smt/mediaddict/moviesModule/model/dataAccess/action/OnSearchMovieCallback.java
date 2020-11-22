package me.smt.mediaddict.moviesModule.model.dataAccess.action;

import java.util.List;

import me.smt.mediaddict.moviesModule.model.Movie;

public interface OnSearchMovieCallback {
    void onSuccess(int page, List<Movie> movies, String query);
    void onError();
    void onEmptyResult();

}
