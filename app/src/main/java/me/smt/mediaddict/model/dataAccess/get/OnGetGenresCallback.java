package me.smt.mediaddict.model.dataAccess.get;

import java.util.List;

import me.smt.mediaddict.model.Genre;

/**
 * Interface que actúa como un notificador de respuesta
 * al ocurrir una acción determinada.
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public interface OnGetGenresCallback {
    /**
     * Método que salta si se han encontrado coincidencias.
     * @param genres listado de géneros.
     */
    void onSuccess(List<Genre> genres);

    /**
     * Método que salta en caso de error.
     */
    void onError();
}
