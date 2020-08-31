package com.example.texting.profileModule;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.example.texting.common.pojo.User;
import com.example.texting.profileModule.events.ProfileEvent;
import com.example.texting.profileModule.model.ProfileInteractor;
import com.example.texting.profileModule.model.ProfileInteractorClass;
import com.example.texting.profileModule.view.ProfileActivity;
import com.example.texting.profileModule.view.ProfileView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 29/08/2020.
 * Derechos Reservados 2020
 */
public class ProfilePresenterClass implements ProfilePresenter {

    private ProfileView view;
    private ProfileInteractor interactor;

    private boolean isEdit = false;
    private User user;

    public ProfilePresenterClass(ProfileView view) {
        this.view = view;
        this.interactor = new ProfileInteractorClass() ;
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        view = null;
    }

    @Override
    public void setupUser(String username, String email, String photoUrl) {
        user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPhotoUrl(photoUrl);

        view.showUserData(username, email, photoUrl);
    }

    @Override
    public void checkMode() {
        if (isEdit){
            view.launchGalley();
        }
    }

    @Override
    public void updateUsername(String username) {
        if (isEdit){
            if (setProgress()){
                view.showProgress();
                interactor.updateUsername(username);
                user.setUsername(username);
            }
        } else {
            isEdit = true;
            view.menuEditMode();
            view.enableUIElements();
        }
    }

    @Override
    public void updateImage(Uri uri) {
        if (setProgress()){
            view.showProgressImage();
            interactor.updateImage(uri, user.getPhotoUrl());
        }
    }

    @Override
    public void result(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case ProfileActivity.RC_PHOTO_PICKER:
                    view.openDialogPreview(data);
                    break;
            }
        }
    }

    @Subscribe
    @Override
    public void onEventListener(ProfileEvent event) {
        if (view != null){
            view.hideProgress();

            switch (event.getTypeEvent()){
                case ProfileEvent.ERROR_USERNAME:
                    view.enableUIElements();
                    view.onError(event.getResMsg());
                    break;
                case ProfileEvent.ERROR_PROFILE:
                case ProfileEvent.ERROR_SERVER:
                case ProfileEvent.ERROR_IMAGE:
                    view.enableUIElements();
                    view.onErrorUpload(event.getResMsg());
                    break;
                case ProfileEvent.SAVE_USERNAME:
                    view.saveUsernameSuccess();
                    saveSuccess();
                    break;
                case ProfileEvent.UPLOAD_IMAGE:
                    view.updateImageSuccess(event.getPhotoUrl());
                    user.setPhotoUrl(event.getPhotoUrl());
                    saveSuccess();
                    break;
            }
        }
    }

    private void saveSuccess() {
        view.menuNormalMode();
        view.setResultOK(user.getUsername(), user.getPhotoUrl());
        isEdit = false;
    }

    private boolean setProgress() {
        if (view != null){
            view.disableUIElements();
            return true;
        }
        return false;
    }
}
