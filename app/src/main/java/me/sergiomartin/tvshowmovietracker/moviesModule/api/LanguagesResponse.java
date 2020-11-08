package me.sergiomartin.tvshowmovietracker.moviesModule.api;

import java.util.List;
import me.sergiomartin.tvshowmovietracker.moviesModule.model.Language;

public class LanguagesResponse {

    private List<Language> languages;

    public List<Language> getLanguages() {
        return languages;
    }

    public void setGenres(List<Language> languages) {
        this.languages = languages;
    }

}