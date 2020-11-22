package me.smt.mediaddict.moviesModule.model.dataAccess.listener;

public interface OnCheckIfMovieExistsListener {
    void onCheckReceived(boolean movieExists);
    void onError(Throwable error);
}
