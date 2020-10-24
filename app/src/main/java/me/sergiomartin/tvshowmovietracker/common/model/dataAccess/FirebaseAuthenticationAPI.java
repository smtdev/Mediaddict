package me.sergiomartin.tvshowmovietracker.common.model.dataAccess;

import com.google.firebase.auth.FirebaseAuth;

import me.sergiomartin.tvshowmovietracker.common.pojo.User;

public class FirebaseAuthenticationAPI {
    private FirebaseAuth mFirebaseAuth;

    private FirebaseAuthenticationAPI() {
        this.mFirebaseAuth = FirebaseAuth.getInstance();
    }

    private static class SingletonHolder {
        private static final FirebaseAuthenticationAPI INSTANCE = new FirebaseAuthenticationAPI();
    }

    public static FirebaseAuthenticationAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public FirebaseAuth getmFirebaseAuth() {
        return this.mFirebaseAuth;
    }

    public User getAuthUser() {
        User user = new User();
        if (mFirebaseAuth != null && mFirebaseAuth.getCurrentUser() != null) {
            user.setUid(mFirebaseAuth.getCurrentUser().getUid());
            user.setUsername(mFirebaseAuth.getCurrentUser().getDisplayName());
            user.setEmail(mFirebaseAuth.getCurrentUser().getEmail());
            user.setUri(mFirebaseAuth.getCurrentUser().getPhotoUrl());
        }
        return user;
    }
}
