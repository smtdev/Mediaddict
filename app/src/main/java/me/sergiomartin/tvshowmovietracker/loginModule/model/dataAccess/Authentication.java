package me.sergiomartin.tvshowmovietracker.loginModule.model.dataAccess;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import me.sergiomartin.tvshowmovietracker.common.model.dataAccess.FirebaseAuthenticationAPI;
import me.sergiomartin.tvshowmovietracker.common.pojo.User;

public class Authentication {
    private FirebaseAuthenticationAPI mAuthenticationAPI;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    public Authentication() {
        mAuthenticationAPI = FirebaseAuthenticationAPI.getInstance();
    }

    public void onResume() {
        mAuthenticationAPI.getmFirebaseAuth().addAuthStateListener(mAuthStateListener);
    }

    public void onPause() {
        if(mAuthStateListener != null) {
            mAuthenticationAPI.getmFirebaseAuth().removeAuthStateListener(mAuthStateListener);
        }
    }

    public void getStatusAuth(StatusAuthCallback callback) {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    callback.onGetUser(user);
                } else {
                    callback.onLaunchUILogin();
                }
            }
        };
    }

    public User getCurrentUser() {
        return mAuthenticationAPI.getAuthUser();
    }
}
