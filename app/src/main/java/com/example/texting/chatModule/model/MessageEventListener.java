package com.example.texting.chatModule.model;

import com.example.texting.common.pojo.Message;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 31/08/2020.
 * Derechos Reservados 2020
 */
public interface MessageEventListener {

    void onMessageReceived(Message message);
    void onError(int resMsg);
}
