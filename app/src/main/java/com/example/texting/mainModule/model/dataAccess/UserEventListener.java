package com.example.texting.mainModule.model.dataAccess;

import com.example.texting.common.pojo.User;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 15/08/2020.
 * Derechos Reservados 2020
 */
public interface UserEventListener {

    void onUserAdded(User user);
    void onUserUpdated(User user);
    void onUserRemoverd(User user);

    void onError(int resMsg);
}
