package me.sergiomartin.tvshowmovietracker.moviesModule.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LanguagesResponse {
    @SerializedName("iso_639_1")
    @Expose
    private String isoStandard;

    @SerializedName("english_name")
    @Expose
    private String englishName;

    @SerializedName("name")
    @Expose
    private String name;

    public String getIsoStandard() {
        return isoStandard;
    }

    public void setIsoStandard(String isoStandard) {
        this.isoStandard = isoStandard;
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
