package com.example.texting.addModule;

import com.example.texting.addModule.events.AddEvent;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 22/08/2020.
 * Derechos Reservados 2020
 */
public interface AddPresenter {

    void onShow();
    void onDestroy();

    void addFriend(String email);
    void onEventListener(AddEvent event);
}
