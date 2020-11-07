package me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess;

import java.util.List;

import me.sergiomartin.tvshowmovietracker.moviesModule.model.Languages;

public interface OnGetLanguagesCallback {
    void onSuccess(List<Languages> genres);

    void onError();
}
