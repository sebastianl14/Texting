package com.example.texting.chatModule.model;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 31/08/2020.
 * Derechos Reservados 2020
 */
public interface LastConnectionEventListener {

    void onSuccess(boolean online, long lastConnection, String uidConnectedFriend);
}
