package me.smt.mediaddict.loginModule;

import android.content.Intent;

import me.smt.mediaddict.loginModule.events.LoginEvent;

public interface LoginPresenter {
    void onCreate();
    void onResume();
    void onPause();
    void onDestroy();

    /**
     * Enviar validación del método OnActivityResult al hacer login
     * desde la interface del Presenter a su clase controladora, LoginPresenterClass.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    void result(int requestCode, int resultCode, Intent data);

    void getStatusAuth();

    void onEventListener(LoginEvent event);
}
