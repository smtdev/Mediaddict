package me.smt.mediaddict.moviesModule.model.dataAccess.get;

import java.util.List;

import me.smt.mediaddict.moviesModule.model.Genre;

public interface OnGetGenresCallback {
    void onSuccess(List<Genre> genres);

    void onError();
}
