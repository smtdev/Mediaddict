package me.smt.mediaddict.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import me.smt.mediaddict.model.Movie;

/**
 * Método que se utiliza para deserializar la respuesta
 * de las peticiones REST relacionadas con los listados
 * de las películas.
 */
public class MoviesListResponse {
    /**
     * Atributo que indica la página correspondiente a la lista.
     */
    @SerializedName("page")
    @Expose
    private int page;

    /**
     * Atributo que indica el total de resultados.
     */
    @SerializedName("total_results")
    @Expose
    private int totalResults;

    /**
     * Atributo que contiene todos los resultados obtenidos.
     */
    @SerializedName("results")
    @Expose
    private List<Movie> movies;

    /**
     * Atributo que indica el total de páginas.
     */
    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    /**
     * Método que devuelve la página actual.
     * @return int la página actual.
     */
    public int getPage() {
        return page;
    }

    /**
     * Método que modifica el número de página actual.
     * @param page número de página.
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * Método que devuelve el total de resultados.
     * @return int total de resultados.
     */
    public int getTotalResults() {
        return totalResults;
    }

    /**
     * Método que modifica el número resultados.
     * @param totalResults número total de resultados.
     */
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    /**
     * Método que devuelve la lista de películas total.
     * @return List<Movie> la lista de películas.
     */
    public List<Movie> getMovies() {
        return movies;
    }

    /**
     * Método que modifica el listado de resultados.
     * @param movies listado de resultados.
     */
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    /**
     * Método que devuelve el total de páginas.
     * @return int total de páginas.
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Método que modifica el número total de páginas.
     * @param totalPages total de páginas.
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}