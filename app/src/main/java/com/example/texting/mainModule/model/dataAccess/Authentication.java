package com.example.texting.mainModule.model.dataAccess;

import com.example.texting.common.model.dataAccess.FirebaseAuthenticationAPI;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 15/08/2020.
 * Derechos Reservados 2020
 */
public class Authentication {

    private FirebaseAuthenticationAPI authenticationAPI;

    public Authentication() {
        authenticationAPI = FirebaseAuthenticationAPI.getInstance();
    }

    public FirebaseAuthenticationAPI getAuthenticationAPI() {
        return authenticationAPI;
    }

    public void signOff(){
        authenticationAPI.getFirebaseAuth().signOut();
    }
}
