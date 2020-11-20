package me.sergiomartin.tvshowmovietracker.common.pojo;

import android.net.Uri;

import com.google.firebase.database.Exclude;

import java.util.Objects;

public class User {
    public static final String USERNAME = "username";
    public static final String PHOTO_URL = "photoUrl";
    public static final String EMAIL = "email";

    private String username;
    private String email;
    private String photoUrl;

    @Exclude
    private String uid;
    @Exclude
    private Uri uri;

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl != null? photoUrl : uri != null? uri.toString() : "";
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Exclude
    public String getUid() {
        return uid;
    }
    @Exclude
    public void setUid(String uid) {
        this.uid = uid;
    }
    @Exclude
    public Uri getUri() {
        return uri;
    }
    @Exclude
    public void setUri(Uri uri) {
        this.uri = uri;
    }
    @Exclude
    public String getUsernameValid(){
        return username == null?
                getEmail() : username.isEmpty()?
                getEmail() : username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return Objects.equals(uid, user.uid);
    }

    @Override
    public int hashCode() {
        return uid != null ? uid.hashCode() : 0;
    }
}
