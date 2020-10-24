package me.sergiomartin.tvshowmovietracker.loginModule.model;

public interface LoginInteractor {
    void onResume();
    void onPause();

    void getStatusAuth();
}
