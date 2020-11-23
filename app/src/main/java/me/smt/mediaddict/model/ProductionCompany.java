package me.smt.mediaddict.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Clase que actúa como modelo de datos de las diferentes
 * productoras que tiene una película.
 * Es utilizada en la deserialización de los datos
 * obtenidos por la API.
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public class ProductionCompany {

    /**
     * Atributo que indica el ID de la productora.
     */
    @SerializedName("id")
    @Expose
    private int id;

    /**
     * Atributo que indica el nombre de la productora.
     */
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * Atributo que indica el path de la productora.
     */
    @SerializedName("logo_path")
    @Expose
    private String logoPath;

    /**
     * Atributo que indica el país de origen de la productora.
     */
    @SerializedName("origin_country")
    @Expose
    private String originCountry;

    /**
     * Método que devuelve el ID de la productora.
     * @return int id de la productora.
     */
    public int getId() {
        return id;
    }

    /**
     * Método que modifica el ID de la productora.
     * @param id id de la productora
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Método que devuelve el nombre de la productora.
     * @return String nombre de la productora.
     */
    public String getName() {
        return name;
    }

    /**
     * Método que modifica el nombre de la productora.
     * @param name nombre de la productora.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Método que devuelve el logo de la productora.
     * @return String logo de la productora.
     */
    public String getLogoPath() {
        return logoPath;
    }

    /**
     * Método que modifica el path del logo de la productora.
     * @param logoPath path del logo de la productora.
     */
    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    /**
     * Método que devuelve el país de la productora.
     * @return String país de la productora.
     */
    public String getOriginCountry() {
        return originCountry;
    }

    /**
     * Método que modifica el país de origen de la productora.
     * @param originCountry país de origen.
     */
    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

}