package com.example.texting.profileModule.model.dataAccess;

import android.app.Activity;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.texting.R;
import com.example.texting.common.model.StorageUploadImageCallback;
import com.example.texting.common.model.dataAccess.FirebaseStorageAPI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 22/08/2020.
 * Derechos Reservados 2020
 */
public class Storage {

    private static final String PATH_PROFILE = "profile";

    private FirebaseStorageAPI storageAPI;

    public Storage() {
        this.storageAPI = FirebaseStorageAPI.getInstance();
    }

    public void uploadImageProfile(Uri imageUri, String email, StorageUploadImageCallback callback){

        if (imageUri.getLastPathSegment() != null){
            final StorageReference photoRef = storageAPI.getPhotosReferenceByEmail(email)
                    .child(PATH_PROFILE).child(imageUri.getLastPathSegment());

            photoRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    if (uri != null) {
                                        callback.onSuccess(uri);
                                    } else {
                                        callback.onError(R.string.profile_error_imageUpdated);
                                    }
                                }
                            });
                }
            });
        } else {
            callback.onError(R.string.profile_error_invalid_image);
        }
    }

    public void deleteOldImage(String oldPhotoUrl, String downloadUrl){
        if (oldPhotoUrl != null && !oldPhotoUrl.isEmpty()){
            StorageReference storageReference = storageAPI.getFirebaseStorage().getReferenceFromUrl(downloadUrl);
            StorageReference oldStorageReference = storageAPI.getFirebaseStorage().getReferenceFromUrl(oldPhotoUrl);

            if (!oldStorageReference.getPath().equals(storageReference.getPath())){
                oldStorageReference.delete().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }
    }
}
