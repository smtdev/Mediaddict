package me.smt.mediaddict.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import me.smt.mediaddict.model.Genre;

/**
 * Método que se utiliza para deserializar la respuesta
 * de las peticiones REST relacionadas con los géneros
 * de las películas.
 */
public class GenresListResponse {
    /**
     * Atributo que se encarga de almacenar un conjunto de géneros.
     */
    @SerializedName("genres")
    @Expose
    private List<Genre> genres;

    /**
     * Método que se encarga de obtener la lista de géneros.
     * @return List<Genre> lista de géneros.
     */
    public List<Genre> getGenres() {
        return genres;
    }

    /**
     * Método que se encarga de modificar la lista de géneros.
     * @param genres lista de géneros.
     */
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
