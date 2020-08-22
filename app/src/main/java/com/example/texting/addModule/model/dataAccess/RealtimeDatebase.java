package com.example.texting.addModule.model.dataAccess;

import androidx.annotation.NonNull;

import com.example.texting.common.model.BasicEventCallback;
import com.example.texting.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.example.texting.common.pojo.User;
import com.example.texting.common.utils.UtilsCommon;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 22/08/2020.
 * Derechos Reservados 2020
 */
public class RealtimeDatebase {

    private FirebaseRealtimeDatabaseAPI databaseAPI;

    public RealtimeDatebase() {
        databaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
    }

    public void addFriend(String email, User myUser, BasicEventCallback callback){
        Map<String, Object> myUserMap = new HashMap<>();
        myUserMap.put(User.USER_NAME, myUser.getUsername());
        myUserMap.put(User.EMAIL, myUser.getEmail());
        myUserMap.put(User.PHOTO_URL, myUser.getPhotoUrl());

        final String emailEnconded = UtilsCommon.getEmailEncoded(email);
        DatabaseReference userReference = databaseAPI.getRequestReference(emailEnconded);
        userReference.child(myUser.getUid()).updateChildren(myUserMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onError();
                    }
                });
    }
}
