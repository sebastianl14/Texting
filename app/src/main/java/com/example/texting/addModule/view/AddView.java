package com.example.texting.addModule.view;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 22/08/2020.
 * Derechos Reservados 2020
 */
public interface AddView {

    void enableUIElements();
    void disableUIElements();
    void showProgress();
    void hideProgress();

    void friendAdded();
    void friendNotAdded();

    void showMessageExist(int resMsg);

}
