package com.example.texting.mainModule.model.dataAccess;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.texting.R;
import com.example.texting.common.model.BasicEventCallback;
import com.example.texting.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.example.texting.common.pojo.User;
import com.example.texting.common.utils.UtilsCommon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 15/08/2020.
 * Derechos Reservados 2020
 */
public class RealtimeDatabase {

    private FirebaseRealtimeDatabaseAPI databaseAPI;

    private ChildEventListener userEventListener;
    private ChildEventListener requestEventListener;

    public RealtimeDatabase() {
        databaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
    }

    /*
    * references
    */

    public FirebaseRealtimeDatabaseAPI getDatabaseAPI() {
        return databaseAPI;
    }

    private DatabaseReference getUserReference(){
        return databaseAPI.getRootReference().child(FirebaseRealtimeDatabaseAPI.PATH_USERS);
    }

    /*
    * public methods
     */

    public void subscribeToUserList(String myUid, final UserEventListener listener){
        if (userEventListener == null){
            userEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    listener.onUserAdded(getUser(dataSnapshot));
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    listener.onUserUpdated(getUser(dataSnapshot));
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    listener.onUserRemoverd(getUser(dataSnapshot));
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    switch (databaseError.getCode()){
                        case DatabaseError.PERMISSION_DENIED:
                            listener.onError(R.string.main_error_permission_denied);
                            break;
                        default:
                            listener.onError(R.string.common_error_server);
                            break;
                    }
                }
            };
        }
        databaseAPI.getContactsReference(myUid).addChildEventListener(userEventListener);
    }

    private User getUser(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        if (user != null){
            user.setUid(dataSnapshot.getKey());
        }
        return user;
    }


    public void subscribeToRequests(String email, final UserEventListener listener){
        if (requestEventListener == null){
            requestEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    listener.onUserAdded(getUser(dataSnapshot));
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    listener.onUserUpdated(getUser(dataSnapshot));
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    listener.onUserRemoverd(getUser(dataSnapshot));
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    listener.onError(R.string.common_error_server);
                }
            };
        }
        final String emailEncoded = UtilsCommon.getEmailEncoded(email);
        databaseAPI.getRequestReference(emailEncoded).addChildEventListener(requestEventListener);
    }


    public void unsubscribeToUser(String uid){
        if (userEventListener != null){
            databaseAPI.getContactsReference(uid).removeEventListener(userEventListener);
        }
    }

    public void unsubscribeToRequest(String email){
        if (requestEventListener != null){
            final String emailEncoded = UtilsCommon.getEmailEncoded(email);
            databaseAPI.getRequestReference(emailEncoded).removeEventListener(userEventListener);
        }
    }

    public void removeUser(String friendUid, String myUid, final BasicEventCallback callback){

        Map<String, Object> removeUserMap = new HashMap<>();
        removeUserMap.put(myUid + "/" + FirebaseRealtimeDatabaseAPI.PATH_CONTACTS + "/"+ friendUid, null);
        removeUserMap.put(friendUid + "/" + FirebaseRealtimeDatabaseAPI.PATH_CONTACTS + "/" + myUid, null);

        getUserReference().updateChildren(removeUserMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null){
                    callback.onSuccess();
                } else {
                    callback.onError();
                }
            }
        });
    }

    public void acceptRequest(User user, User myUser, final BasicEventCallback callback){
        Map<String, String> userRequestMap = new HashMap<>();
        userRequestMap.put(User.USER_NAME, user.getUsername());
        userRequestMap.put(User.EMAIL, user.getEmail());
        userRequestMap.put(User.PHOTO_URL, user.getPhotoUrl());

        Map<String, String> myUserMap = new HashMap<>();
        myUserMap.put(User.USER_NAME, myUser.getUsername());
        myUserMap.put(User.EMAIL, myUser.getEmail());
        myUserMap.put(User.PHOTO_URL, myUser.getPhotoUrl());

        final String emialEnconded = UtilsCommon.getEmailEncoded(myUser.getEmail());

        Map<String, Object> acceptRequest = new HashMap<>();
        acceptRequest.put(FirebaseRealtimeDatabaseAPI.PATH_USERS + "/" + user.getUid() + "/" +
                FirebaseRealtimeDatabaseAPI.PATH_CONTACTS + "/" + myUser.getUid(), myUserMap);
        acceptRequest.put(FirebaseRealtimeDatabaseAPI.PATH_USERS + "/" + myUser.getUid() + "/" +
                FirebaseRealtimeDatabaseAPI.PATH_CONTACTS + "/" + user.getUid(), userRequestMap);
        acceptRequest.put(FirebaseRealtimeDatabaseAPI.PATH_REQUEST + "/" + emialEnconded + "/" +
                user.getUid(), null);

        databaseAPI.getRootReference().updateChildren(acceptRequest, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null){
                    callback.onSuccess();
                } else {
                    callback.onError();
                }
            }
        });
    }

    public void denyRequest(User user, String myEmail, final BasicEventCallback callback){
         final String emailEncoded = UtilsCommon.getEmailEncoded(myEmail);
         databaseAPI.getRequestReference(emailEncoded).child(user.getUid())
                 .removeValue(new DatabaseReference.CompletionListener() {
                     @Override
                     public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                         if (databaseError == null){
                             callback.onSuccess();
                         } else {
                             callback.onError();
                         }
                     }
                 });
    }

}
