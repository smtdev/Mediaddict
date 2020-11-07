package me.sergiomartin.tvshowmovietracker.moviesModule.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Languages {
    @SerializedName("iso_639_1")
    @Expose
    private String langIsoStandard;

    @SerializedName("name")
    @Expose
    private String name;

    public String getLangIsoStandard() {
        return langIsoStandard;
    }

    public void setLangIsoStandard(String langIsoStandard) {
        this.langIsoStandard = langIsoStandard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
