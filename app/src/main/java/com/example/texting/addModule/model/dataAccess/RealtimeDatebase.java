package com.example.texting.addModule.model.dataAccess;

import androidx.annotation.NonNull;

import com.example.texting.R;
import com.example.texting.addModule.events.AddEvent;
import com.example.texting.common.model.BasicEventCallback;
import com.example.texting.common.model.EventsCallback;
import com.example.texting.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.example.texting.common.pojo.User;
import com.example.texting.common.utils.UtilsCommon;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

    public void checkUserExist(String email, EventsCallback callback){
        DatabaseReference usersReference = databaseAPI.getRootReference()
                .child(FirebaseRealtimeDatabaseAPI.PATH_USERS);

        Query userByEmailQuery = usersReference.orderByChild(User.EMAIL).equalTo(email).limitToFirst(1);
        userByEmailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    callback.onSuccess();
                } else {
                    callback.onError(AddEvent.ERROR_EXIST, R.string.addFriend_error_user_exist);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(AddEvent.ERROR_SERVER, R.string.addFriend_error_message);
            }
        });
    }

    public void checkRequestNoExist(String email, String myUid, EventsCallback callback){
        String emailEncoded = UtilsCommon.getEmailEncoded(email);
        DatabaseReference myRequestReference = databaseAPI.getRequestReference(emailEncoded).child(myUid);

        myRequestReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    callback.onError(AddEvent.ERROR_EXIST, R.string.addFriend_message_request_exist);
                } else {
                    callback.onSuccess();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(AddEvent.ERROR_SERVER, R.string.addFriend_error_message);
            }
        });
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
