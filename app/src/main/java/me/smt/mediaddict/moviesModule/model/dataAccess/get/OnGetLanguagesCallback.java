package me.smt.mediaddict.moviesModule.model.dataAccess.get;

import java.util.List;

import me.smt.mediaddict.moviesModule.model.Language;

public interface OnGetLanguagesCallback {
    void onSuccess(List<Language> language);

    void onError();
}
