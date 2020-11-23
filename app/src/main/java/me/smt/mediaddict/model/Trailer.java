package me.smt.mediaddict.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Clase que actúa como modelo de datos de los diferentes
 * tráilers de una película.
 * Es utilizada en la deserialización de los datos
 * obtenidos por la API.
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public class Trailer {
    /**
     * Atributo que actúa de clave de un tráiler.
     */
    @SerializedName("key")
    @Expose
    private String key;

    /**
     * Método que devuelve el ID del tráiler.
     * @return String id del tráiler.
     */
    public String getKey() {
        return key;
    }

    /**
     * Método que se encarga de modificar el ID del tráiler.
     * @param key id del tráiler.
     */
    public void setKey(String key) {
        this.key = key;
    }
}