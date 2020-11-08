package me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.get;

import java.util.List;

import me.sergiomartin.tvshowmovietracker.moviesModule.model.Genre;

public interface OnGetGenresCallback {
    void onSuccess(List<Genre> genres);

    void onError();
}
