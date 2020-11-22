package me.smt.mediaddict.loginModule.model;

import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;

import me.smt.mediaddict.common.pojo.User;
import me.smt.mediaddict.loginModule.events.LoginEvent;
import me.smt.mediaddict.loginModule.model.dataAccess.Authentication;
import me.smt.mediaddict.loginModule.model.dataAccess.RealtimeDatabase;
import me.smt.mediaddict.loginModule.model.dataAccess.StatusAuthCallback;

public class LoginInteractorClass implements LoginInteractor {
    private Authentication mAuthentication;
    private RealtimeDatabase mDatabase;

    public LoginInteractorClass() {
        mAuthentication = new Authentication();
        mDatabase = new RealtimeDatabase();
    }

    @Override
    public void onResume() {
        mAuthentication.onResume();
    }

    @Override
    public void onPause() {
        mAuthentication.onPause();
    }

    @Override
    public void getStatusAuth() {
        mAuthentication.getStatusAuth(new StatusAuthCallback() {
            @Override
            public void onGetUser(FirebaseUser user) {
                post(LoginEvent.STATUS_AUTH_SUCCESS, user);

                mDatabase.checkUserExists(mAuthentication.getCurrentUser().getUid(), (typeEvent, resMsg) -> {
                    if (typeEvent == LoginEvent.USER_NOT_EXISTS) {
                        registerUser();
                    } else {
                        post(typeEvent);
                    }
                });
            }

            @Override
            public void onLaunchUILogin() {
                post(LoginEvent.STATUS_AUTH_ERROR);
            }
        });
    }

    private void registerUser() {
        User currentUser = mAuthentication.getCurrentUser();
        mDatabase.registerUser(currentUser);
    }

    private void post(int typeEvent) {
        post(typeEvent, null);
    }

    private void post(int typeEvent, FirebaseUser user) {
        LoginEvent event = new LoginEvent();
        event.setTypeEvent(typeEvent);
        event.setUser(user);

        EventBus.getDefault().post(event);
    }
}
