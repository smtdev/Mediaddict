package me.smt.mediaddict.model.dataAccess.get;

import me.smt.mediaddict.model.Movie;

/**
 * Interface que actúa como un notificador de respuesta
 * al ocurrir una acción determinada.
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public interface OnGetMovieCallback {

    /**
     * Método que salta en caso de una respuesta positiva.
     * @param movie película buscada.
     */
    void onSuccess(Movie movie);
    /**
     * Método que salta en caso de error.
     */
    void onError();
}
