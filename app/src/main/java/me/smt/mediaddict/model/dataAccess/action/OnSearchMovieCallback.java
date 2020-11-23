package me.smt.mediaddict.model.dataAccess.action;

import java.util.List;

import me.smt.mediaddict.model.Movie;

/**
 * Interface que actúa como un notificador de respuesta
 * al ocurrir una acción determinada.
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public interface OnSearchMovieCallback {
    /**
     * Método que salta si existen resultados de búsqueda.
     * @param page número de página.
     * @param movies listado de película.
     * @param query película buscada.
     */
    void onSuccess(int page, List<Movie> movies, String query);

    /**
     * Método que salta si ha habido algún error en la búsqueda.
     */
    void onError();

    /**
     * Método que salta si no ha habido resultados en la búsqueda.
     */
    void onEmptyResult();
}
