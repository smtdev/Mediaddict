package me.smt.mediaddict.model.dataAccess.listener;

/**
 * Interface que actúa de listener notificando
 * sobre la existencia de una película en Firebase.
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public interface OnCheckIfMovieExistsListener {
    /**
     * Método que salta si la llamada ha sido correcta.
     * @param movieExists el estado de la existencia de la película.
     */
    void onCheckReceived(boolean movieExists);

    /**
     * Método que salta en caso de error.
     * @param error error en cuestión.
     */
    void onError(Throwable error);
}
