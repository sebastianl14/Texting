package com.example.texting.profileModule.model.dataAccess;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.texting.R;
import com.example.texting.common.model.EventErrorTypeListener;
import com.example.texting.common.model.StorageUploadImageCallback;
import com.example.texting.common.model.dataAccess.FirebaseAuthenticationAPI;
import com.example.texting.common.pojo.User;
import com.example.texting.profileModule.events.ProfileEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 22/08/2020.
 * Derechos Reservados 2020
 */
public class Authentication {

    private FirebaseAuthenticationAPI authenticationAPI;

    public Authentication() {
        authenticationAPI = FirebaseAuthenticationAPI.getInstance();
    }

    public FirebaseAuthenticationAPI getAuthenticationAPI() {
        return authenticationAPI;
    }

    public void updateUsernameFirebaseProfile(User myUser, EventErrorTypeListener listener){
        FirebaseUser user = authenticationAPI.getCurrentUser();
        if (user != null){
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(myUser.getUsername())
                    .build();

            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        listener.onError(ProfileEvent.ERROR_PROFILE, R.string.profile_error_userUpdated);
                    }
                }
            });
        }
    }

    public void updateImageFirebaseProfile(Uri downloadUri, StorageUploadImageCallback callback){
        FirebaseUser user = authenticationAPI.getCurrentUser();
        if (user != null){
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(downloadUri)
                    .build();

            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        callback.onSuccess(downloadUri);
                    } else {
                        callback.onError(R.string.profile_error_imageUpdated);
                    }
                }
            });
        }
    }
}
