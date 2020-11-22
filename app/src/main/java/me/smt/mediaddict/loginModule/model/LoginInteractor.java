package me.smt.mediaddict.loginModule.model;

public interface LoginInteractor {
    void onResume();
    void onPause();

    void getStatusAuth();
}
