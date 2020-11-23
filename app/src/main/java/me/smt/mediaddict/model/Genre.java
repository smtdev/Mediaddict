package me.smt.mediaddict.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Clase que actúa como modelo de datos de los diferentes
 * géneros que tiene una película.
 * Es utilizada en la deserialización de los datos
 * obtenidos por la API.
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public class Genre {

    /**
     * Atributo que indica el ID del género.
     */
    @SerializedName("id")
    @Expose
    private Integer id;

    /**
     * Atributo que indica el nombre del género.
     */
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * Método que devuelve el ID del género.
     * @return Integer ID del género.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Método que modifica el ID del género.
     * @param id ID del género.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Método que devuelve el nombre del género.
     * @return String nombre del género.
     */
    public String getName() {
        return name;
    }

    /**
     * Método que modifica el nombre del género.
     * @param name nombre del género.
     */
    public void setName(String name) {
        this.name = name;
    }

}