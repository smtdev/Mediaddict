package me.smt.mediaddict.moviesModule.model.dataAccess.get;

import java.util.List;

import me.smt.mediaddict.moviesModule.model.Movie;

public interface OnGetMoviesCallback {
    void onSuccess(int page, List<Movie> movies);
    void onError();
}
