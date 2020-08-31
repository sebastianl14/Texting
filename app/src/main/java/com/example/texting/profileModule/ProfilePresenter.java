package com.example.texting.profileModule;

import android.content.Intent;
import android.net.Uri;

import com.example.texting.profileModule.events.ProfileEvent;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 22/08/2020.
 * Derechos Reservados 2020
 */
public interface ProfilePresenter {

    void onCreate();
    void onDestroy();

    void setupUser(String username, String email, String photoUrl);
    void checkMode();

    void updateUsername(String username);
    void updateImage(Uri uri);

    void result(int requestCode, int resultCode, Intent data);

    void onEventListener(ProfileEvent event);
}
