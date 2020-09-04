package com.example.texting.chatModule.model;

import android.app.Activity;
import android.net.Uri;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 31/08/2020.
 * Derechos Reservados 2020
 */
public interface ChatInteractor {

    void subscribeToFriend(String friendUid, String friendEmail);
    void unsubscribeToFriend(String friendUid);

    void subscribeToMessages();
    void unsubscribeToMessages();

    void sendMessage(String msg);
    void sendImage(Activity activity, Uri imageUri);
}
