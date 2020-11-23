package me.smt.mediaddict.model.dataAccess.action;

import android.widget.ImageView;

import me.smt.mediaddict.model.Movie;

/**
 * Interface que actúa como un notificador de respuesta
 * al ocurrir una acción determinada
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public interface OnMoviesClickCallback {
    /**
     * Método utilizado para la transición del Home a los detalles
     * de la película
     * @param movie la película a la que ver los detalles
     * @param movieImageView el posterpath de la película
     */
    void onClick(Movie movie, ImageView movieImageView);
}
