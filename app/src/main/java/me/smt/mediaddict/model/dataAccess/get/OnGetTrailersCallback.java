package me.smt.mediaddict.model.dataAccess.get;

import java.util.List;

import me.smt.mediaddict.model.Trailer;

/**
 * Interface que actúa como un notificador de respuesta
 * al ocurrir una acción determinada.
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public interface OnGetTrailersCallback {
    /**
     * Método que salta si hay coincidencia en la búsqueda
     * de tráilers relacionados con la película.
     * @param trailers listado de tráilers.
     */
    void onSuccess(List<Trailer> trailers);

    /**
     * Método que salta en caso de error.
     */
    void onError();
}