package com.example.texting.common.model.dataAccess;

import com.example.texting.common.pojo.User;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthenticationAPI {

    private FirebaseAuth firebaseAuth;

    private static class SingletonHolder{

        private static final FirebaseAuthenticationAPI INSTANCE = new FirebaseAuthenticationAPI();


    }
    public static FirebaseAuthenticationAPI getInstance(){
        return SingletonHolder.INSTANCE;
    }
    private FirebaseAuthenticationAPI() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getFirebaseAuth() {
        return this.firebaseAuth;
    }

    public User getAuthUser() {

        User user = new User();
        if (firebaseAuth != null && firebaseAuth.getCurrentUser() != null){
            user.setUid(firebaseAuth.getCurrentUser().getUid());
            user.setUsername(firebaseAuth.getCurrentUser().getDisplayName());
            user.setEmail(firebaseAuth.getCurrentUser().getEmail());
            user.setUri(firebaseAuth.getCurrentUser().getPhotoUrl());
        }
        return user;
    }
}
