package me.sergiomartin.tvshowmovietracker.loginModule;

import android.content.Intent;

import me.sergiomartin.tvshowmovietracker.loginModule.events.LoginEvent;

public interface LoginPresenter {
    void onCreate();
    void onResume();
    void onPause();
    void onDestroy();

    void result(int requestCode, int resultCode, Intent data);

    void getStatusAuth();

    void onEventListener(LoginEvent event);
}
