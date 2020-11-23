package me.smt.mediaddict.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Clase que actúa como modelo de datos de los diferentes
 * lenguajes que tiene una película.
 * Es utilizada en la deserialización de los datos
 * obtenidos por la API.
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public class Language {
    /**
     * Atributo que indica el estándar ISO del lenguaje.
     */
    @SerializedName("iso_639_1")
    @Expose
    private String langIsoStandard;

    /**
     * Atributo que indica el nombre del lenguaje en idioma inglés
     */
    @SerializedName("english_name")
    @Expose
    private String englishName;

    /**
     * Atributo que indica el nombre del lenguaje.
     */
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * Método que devuelve el estándar ISO.
     * @return String estándar ISO.
     */
    public String getLangIsoStandard() {
        return langIsoStandard;
    }

    /**
     * Método que modifica el estándar ISO.
     * @param langIsoStandard estándar ISO.
     */
    public void setLangIsoStandard(String langIsoStandard) {
        this.langIsoStandard = langIsoStandard;
    }

    /**
     * Método que devuelve el nombre del lenguaje en inglés.
     * @return String nombre del lenguaje en inglés.
     */
    public String getEnglishName() {
        return englishName;
    }

    /**
     * Método que modifica el nombre del lenguaje en inglés.
     * @param englishName nombre del lenguaje en inglés.
     */
    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    /**
     * Método que devuelve el nombre del lenguaje.
     * @return String nombre del lenguaje.
     */
    public String getName() {
        return name;
    }

    /**
     * Método que modifica el nombre del lenguaje.
     * @param name nombre del lenguaje.
     */
    public void setName(String name) {
        this.name = name;
    }
}
