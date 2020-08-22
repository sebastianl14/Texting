package com.example.texting.loginModule.model.dataAccess;

import androidx.annotation.NonNull;

import com.example.texting.R;
import com.example.texting.common.model.EventErrorTypeListener;
import com.example.texting.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.example.texting.common.pojo.User;
import com.example.texting.loginModule.events.LoginEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RealtimeDatabase {

    private FirebaseRealtimeDatabaseAPI databaseAPI;

    public RealtimeDatabase() {
        databaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
    }

    public void registerUser(User user){
        Map<String, Object> values = new HashMap<>();
        values.put(User.USER_NAME, user.getUsername());
        values.put(User.EMAIL, user.getEmail());
        values.put(User.PHOTO_URL, user.getPhotoUrl());

        databaseAPI.getUserReferenceByUid(user.getUid()).updateChildren(values);
    }

    public void checkUserExist(String uid, EventErrorTypeListener listener){

        databaseAPI.getUserReferenceByUid(uid).child(User.EMAIL)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()){
                            listener.onError(LoginEvent.USER_NOT_EXIST, R.string.login_error_user_exist);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        listener.onError(LoginEvent.ERROR_SERVER, R.string.login_message_error);
                    }
                });

    }
}
