package com.example.texting.profileModule.model;

import android.app.Activity;
import android.net.Uri;

import com.example.texting.common.model.EventErrorTypeListener;
import com.example.texting.common.model.StorageUploadImageCallback;
import com.example.texting.common.pojo.User;
import com.example.texting.profileModule.events.ProfileEvent;
import com.example.texting.profileModule.model.dataAccess.Authentication;
import com.example.texting.profileModule.model.dataAccess.RealtimeDatabase;
import com.example.texting.profileModule.model.dataAccess.Storage;
import com.example.texting.profileModule.model.dataAccess.UpdateUserListener;

import org.greenrobot.eventbus.EventBus;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 22/08/2020.
 * Derechos Reservados 2020
 */
public class ProfileInteractorClass implements ProfileInteractor {

    private Authentication authentication;
    private RealtimeDatabase database;
    private Storage storage;

    private User myUser;

    public ProfileInteractorClass() {
        authentication = new Authentication();
        database = new RealtimeDatabase();
        storage = new Storage();
    }

    private User getCurrentUser(){
        if (myUser == null){
            myUser = authentication.getAuthenticationAPI().getAuthUser();
        }
        return myUser;
    }

    @Override
    public void updateUsername(String username) {
        User miUser = getCurrentUser();
        miUser.setUsername(username);

        database.changeUsername(miUser, new UpdateUserListener() {
            @Override
            public void onSuccess() {
                authentication.updateUsernameFirebaseProfile(miUser, new EventErrorTypeListener() {
                    @Override
                    public void onError(int typeEvent, int rsgMsg) {
                        post(typeEvent, null, rsgMsg);
                    }
                });
            }

            @Override
            public void onNotifyContacts() {
                postUsernameSuccess();
            }

            @Override
            public void onError(int resMsg) {
                post(ProfileEvent.ERROR_USERNAME, null, resMsg);
            }
        });
    }

    @Override
    public void updateImage(Uri uri, String oldPhotoUrl) {
        storage.uploadImageProfile(uri, getCurrentUser().getEmail(), new StorageUploadImageCallback() {
            @Override
            public void onSuccess(Uri uri) {
                database.updatePhotoUrl(uri, getCurrentUser().getUid(), new StorageUploadImageCallback() {
                    @Override
                    public void onSuccess(Uri newUri) {
                        post(ProfileEvent.UPLOAD_IMAGE, newUri.toString(), 0);
                    }

                    @Override
                    public void onError(int resMsg) {
                        post(ProfileEvent.ERROR_SERVER, resMsg);
                    }
                });

                authentication.updateImageFirebaseProfile(uri, new StorageUploadImageCallback() {
                    @Override
                    public void onSuccess(Uri newUri) {
                        storage.deleteOldImage(oldPhotoUrl, newUri.toString());
                    }

                    @Override
                    public void onError(int resMsg) {
                        post(ProfileEvent.ERROR_PROFILE, resMsg);
                    }
                });
            }

            @Override
            public void onError(int resMsg) {
                post(ProfileEvent.ERROR_IMAGE, resMsg);
            }
        });
    }

    private void post(int typeEvent, int resMsg) {
        post(typeEvent, null, resMsg);
    }

    private void postUsernameSuccess() {
        post(ProfileEvent.SAVE_USERNAME, null, 0);
    }

    private void post(int typeEvent, String photoUrl, int rsgMsg) {
        ProfileEvent event = new ProfileEvent();
        event.setPhotoUrl(photoUrl);
        event.setResMsg(rsgMsg);
        event.setTypeEvent(typeEvent);

        EventBus.getDefault().post(event);
    }
}
