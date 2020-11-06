package me.sergiomartin.tvshowmovietracker.moviesModule.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductionCountry {

    @SerializedName("iso_3166_1")
    @Expose
    private String countryIsoStandard;

    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryIsoStandard() {
        return countryIsoStandard;
    }

    public void setCountryIsoStandard(String countryIsoStandard) {
        this.countryIsoStandard = countryIsoStandard;
    }
}
