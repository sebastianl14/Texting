package com.example.texting.profileModule.model.dataAccess;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 22/08/2020.
 * Derechos Reservados 2020
 */
public interface UpdateUserListener {

    void onSuccess();
    void onNotifyContacts();
    void onError(int resMsg);
}
