package com.example.texting.mainModule.view.adapters;

import com.example.texting.common.pojo.User;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 15/08/2020.
 * Derechos Reservados 2020
 */
public interface OnItemClickListener {

    void onItemClick(User user);
    void onItemLongClick(User user);

    void onAcceptRequest(User user);
    void onDenyRequest(User user);
}
