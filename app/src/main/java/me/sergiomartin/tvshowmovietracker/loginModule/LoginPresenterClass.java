package me.sergiomartin.tvshowmovietracker.loginModule;

import android.app.Activity;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.sergiomartin.tvshowmovietracker.R;
import me.sergiomartin.tvshowmovietracker.loginModule.events.LoginEvent;
import me.sergiomartin.tvshowmovietracker.loginModule.model.LoginInteractor;
import me.sergiomartin.tvshowmovietracker.loginModule.model.LoginInteractorClass;
import me.sergiomartin.tvshowmovietracker.loginModule.view.LoginActivity;
import me.sergiomartin.tvshowmovietracker.loginModule.view.LoginView;

public class LoginPresenterClass implements LoginPresenter {
    private LoginView mView;
    private LoginInteractor mInteractor;

    public LoginPresenterClass(LoginView mView) {
        this.mView = mView;
        this.mInteractor = new LoginInteractorClass();
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        if (setProgress()) {
            mInteractor.onResume();
        }
    }

    @Override
    public void onPause() {
        if(setProgress()) {
            mInteractor.onPause();
        }
    }

    private boolean setProgress() {
        if (mView != null) {
            mView.showProgress();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        mView = null;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void result(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case LoginActivity.RC_SIGN_IN:
                    if (data != null) {
                        mView.showLoginSuccessfully(data);
                    }
                    break;
            }
        } else {
            mView.showError(R.string.login_message_error);
        }
    }

    @Override
    public void getStatusAuth() {
        if (setProgress()) {
            mInteractor.getStatusAuth();
        }
    }

    @Subscribe
    @Override
    public void onEventListener(LoginEvent event) {
        if (mView != null) {
            mView.hideProgress();

            switch (event.getTypeEvent()) {
                case LoginEvent.STATUS_AUTH_SUCCESS:
                    if(setProgress()) {
                        mView.showMessageStarting();
                        mView.openMainActivity();
                    }
                    break;
                case LoginEvent.STATUS_AUTH_ERROR:
                    mView.openUILogin();
                    break;
                case LoginEvent.ERROR_SERVER:
                    mView.showError(event.getResMsg());
                    break;
            }
        }
    }
}
