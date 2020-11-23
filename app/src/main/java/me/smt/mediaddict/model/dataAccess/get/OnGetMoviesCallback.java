package me.smt.mediaddict.model.dataAccess.get;

import java.util.List;

import me.smt.mediaddict.model.Movie;

/**
 * Interface que actúa como un notificador de respuesta
 * al ocurrir una acción determinada.
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public interface OnGetMoviesCallback {
    /**
     * Método que salta si se han encontrado coincidencias.
     * @param page número de página.
     * @param movies listado de películas.
     */
    void onSuccess(int page, List<Movie> movies);

    /**
     * Método que salta en caso de error.
     */
    void onError();
}
