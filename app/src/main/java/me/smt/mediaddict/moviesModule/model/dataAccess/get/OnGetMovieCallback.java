package me.smt.mediaddict.moviesModule.model.dataAccess.get;

import me.smt.mediaddict.moviesModule.model.Movie;

public interface OnGetMovieCallback {
    void onSuccess(Movie movie);
    void onError();
}
