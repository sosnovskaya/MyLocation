package com.margaritasosnovskaya.mylocation.model;

import java.io.Serializable;

public class User implements Serializable {
    private String mEmail;
    private String mLogin;
    private String mPassword;
    private String mPhotoUri;

    private boolean mHasSuccessLogin;

    public User(String email,String login, String password) {
        mEmail = email;
        mLogin = login;
        mPassword = password;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        mLogin = login;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getPhotoUri() {
        return mPhotoUri;
    }

    public void setPhotoUri(String photoUri) {
        mPhotoUri = photoUri;
    }

    public boolean hasSuccessLogin() {
        return mHasSuccessLogin;
    }

    public void setHasSuccessLogin(boolean hasSuccessLogin) {
        mHasSuccessLogin = hasSuccessLogin;
    }
}

