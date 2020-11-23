package me.smt.mediaddict.model.dataAccess.get;

import java.util.List;

import me.smt.mediaddict.model.Language;

/**
 * Interface que actúa como un notificador de respuesta
 * al ocurrir una acción determinada.
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public interface OnGetLanguagesCallback {
    /**
     * Método que salta en caso de encontrar coincidencias
     * de idiomas.
     * @param language listado de idiomas.
     */
    void onSuccess(List<Language> language);

    /**
     * Método que salta en caso de error.
     */
    void onError();
}
