package me.smt.mediaddict.moviesModule.api;

import java.util.List;
import me.smt.mediaddict.moviesModule.model.Language;

public class LanguagesResponse {

    private List<Language> languages;

    public List<Language> getLanguages() {
        return languages;
    }

    public void setGenres(List<Language> languages) {
        this.languages = languages;
    }

}