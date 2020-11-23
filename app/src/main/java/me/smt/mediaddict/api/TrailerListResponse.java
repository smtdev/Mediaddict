package me.smt.mediaddict.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import me.smt.mediaddict.model.Trailer;

/**
 * Método que se utiliza para deserializar la respuesta
 * de las peticiones REST relacionadas con los tráilers
 * de las películas.
 */
public class TrailerListResponse {
    /**
     * Atributo que se encarga de almacenar un conjunto de tráilers.
     */
    @SerializedName("results")
    @Expose
    private List<Trailer> trailers;

    /**
     * Método que devuelve el listado de tráilers.
     * @return List<Trailer> listado de tráilers.
     */
    public List<Trailer> getTrailers() {
        return trailers;
    }

    /**
     * Método que modifica el listado de tráilers.
     * @param trailers listado de tráilers.
     */
    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }
}