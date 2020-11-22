package me.smt.mediaddict.moviesModule.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Language {
    @SerializedName("iso_639_1")
    @Expose
    private String langIsoStandard;

    @SerializedName("english_name")
    @Expose
    private String englishName;

    @SerializedName("name")
    @Expose
    private String name;

    public String getLangIsoStandard() {
        return langIsoStandard;
    }

    public void setLangIsoStandard(String langIsoStandard) {
        this.langIsoStandard = langIsoStandard;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
