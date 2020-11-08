package me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.get;

import java.util.List;

import me.sergiomartin.tvshowmovietracker.moviesModule.model.Language;

public interface OnGetLanguagesCallback {
    void onSuccess(List<Language> language);

    void onError();
}
