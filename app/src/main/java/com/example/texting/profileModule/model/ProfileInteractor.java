package com.example.texting.profileModule.model;

import android.net.Uri;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 22/08/2020.
 * Derechos Reservados 2020
 */
public interface ProfileInteractor {

    void updateUsername(String username);
    void updateImage(Uri uri, String oldPhotoUrl);
}
