package com.example.texting.profileModule.view;

import android.content.Intent;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 22/08/2020.
 * Derechos Reservados 2020
 */
public interface ProfileView {

    void enableUIElements();
    void disableUIElements();

    void showProgress();
    void hideProgress();
    void showProgressImage();
    void hideProgressImage();

    void showUserData(String username, String email, String photoUrl);
    void launchGalley();
    void openDialogPreview(Intent data);

    void menuEditMode();
    void menuNormalMode();

    void saveUsernameSuccess();
    void updateImageSuccess(String photoUrl);
    void setResultOK(String username, String photoUrl);

    void onErrorUpload(int resMsg);
    void onError(int resMsg);

}
