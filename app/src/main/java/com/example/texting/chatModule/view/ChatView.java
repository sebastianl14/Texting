package com.example.texting.chatModule.view;

import android.content.Intent;

import com.example.texting.common.pojo.Message;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 31/08/2020.
 * Derechos Reservados 2020
 */
public interface ChatView {

    void showProgrees();
    void hideProgress();

    void onStatusUser(boolean connected, long lastConection);

    void onError(int resMsg);

    void onMessageReceived(Message msg);

    void openDialogPreview(Intent data);

}
