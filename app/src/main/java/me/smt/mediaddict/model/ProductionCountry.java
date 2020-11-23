package me.smt.mediaddict.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Clase que actúa como modelo de datos de los diferentes
 * país de producción.
 * Es utilizada en la deserialización de los datos
 * obtenidos por la API.
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public class ProductionCountry {

    /**
     * Atributo que indica el estándar ISO del país.
     */
    @SerializedName("iso_3166_1")
    @Expose
    private String countryIsoStandard;

    /**
     * Atributo que indica el nombre del país.
     */
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * Método que devuelve el nombre del país.
     * @return String nombre del país.
     */
    public String getName() {
        return name;
    }

    /**
     * Método que modifica el nombre del país.
     * @param name nombre del país.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Método que devuelve el estándar ISO.
     * @return String estándar ISO.
     */
    public String getCountryIsoStandard() {
        return countryIsoStandard;
    }

    /**
     * Método que modifica el estándar ISO.
     * @param countryIsoStandard estándar ISO.
     */
    public void setCountryIsoStandard(String countryIsoStandard) {
        this.countryIsoStandard = countryIsoStandard;
    }
}
