package com.example.texting.chatModule;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.example.texting.chatModule.events.ChatEvent;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 31/08/2020.
 * Derechos Reservados 2020
 */
public interface ChatPresenter {

    void onCreate();
    void onDestroy();
    void onPause();
    void onResume();

    void setupFriend(String uid, String email);

    void sendMessage(String msg);
    void sendImage(Activity activity, Uri imageUri);

    void result(int requestCode, int resultCode, Intent data);

    void onEventListener(ChatEvent event);
}
