package com.example.texting.loginModule.model.dataAccess;

import androidx.annotation.NonNull;

import com.example.texting.common.model.dataAccess.FirebaseAuthenticationAPI;
import com.example.texting.common.pojo.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Authentication {

    private FirebaseAuthenticationAPI authenticationAPI;
    private FirebaseAuth.AuthStateListener authStateListener;

    public Authentication() {
        authenticationAPI = FirebaseAuthenticationAPI.getInstance();

    }

    public void onResume(){
        authenticationAPI.getFirebaseAuth().addAuthStateListener(authStateListener);
    }

    public void onPause(){
        if (authStateListener != null){
            authenticationAPI.getFirebaseAuth().removeAuthStateListener(authStateListener);
        }
    }

    public void getStatusAuth(StatusAuthCallback callback){
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    callback.onGetUser(user);
                } else {
                    callback.onLauchUILogin();
                }
            }
        };
    }

    public User getCurrentUser(){

        return authenticationAPI.getAuthUser();
    }
}
