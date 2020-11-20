package me.sergiomartin.tvshowmovietracker.moviesModule.model.dataAccess.listener;

public interface OnCheckIfMovieExistsListener {
    void onCheckReceived(boolean movieExists);
    void onError(Throwable error);
}
