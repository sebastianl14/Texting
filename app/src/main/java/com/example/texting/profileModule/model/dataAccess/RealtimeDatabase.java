package com.example.texting.profileModule.model.dataAccess;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.texting.R;
import com.example.texting.common.model.StorageUploadImageCallback;
import com.example.texting.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.example.texting.common.pojo.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 22/08/2020.
 * Derechos Reservados 2020
 */
public class RealtimeDatabase {

    private FirebaseRealtimeDatabaseAPI databaseAPI;

    public RealtimeDatabase() {
        this.databaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
    }

    public void changeUsername(User myUser, UpdateUserListener listener){

        if (databaseAPI.getUserReferenceByUid(myUser.getUid()) != null){
            Map<String, Object> updates = new HashMap<>();
            updates.put(User.USER_NAME, myUser.getUsername());
            databaseAPI.getUserReferenceByUid(myUser.getUid()).updateChildren(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            listener.onSuccess();
                            notifyContactsUsername(myUser, listener);
                        }
                    });
        }
    }

    private void notifyContactsUsername(User myUser, UpdateUserListener listener) {
        databaseAPI.getContactsReference(myUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child: dataSnapshot.getChildren()){
                            String friendUid = child.getKey();
                            DatabaseReference reference = getContactsReference(friendUid, myUser.getUid());
                            Map<String, Object> updates = new HashMap<>();
                            updates.put(User.USER_NAME, myUser.getUsername());
                            reference.updateChildren(updates);
                        }
                        listener.onNotifyContacts();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        listener.onError(R.string.profile_error_userUpdated);
                    }
                });
    }

    private DatabaseReference getContactsReference(String mainUid, String childUid) {
        return databaseAPI.getUserReferenceByUid(mainUid)
                .child(FirebaseRealtimeDatabaseAPI.PATH_CONTACTS).child(childUid);
    }


    public void updatePhotoUrl(Uri downloadUri, String myUid, StorageUploadImageCallback callback){

        if (databaseAPI.getUserReferenceByUid(myUid) != null){
            Map<String, Object> updates = new HashMap<>();
            updates.put(User.PHOTO_URL, downloadUri.toString());

            databaseAPI.getUserReferenceByUid(myUid).updateChildren(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            callback.onSuccess(downloadUri);
                            notifyContactsPhoto(downloadUri.toString(), myUid, callback);
                        }
                    });
        }
    }

    private void notifyContactsPhoto(String photoUrl, String myUid, StorageUploadImageCallback callback) {
        databaseAPI.getContactsReference(myUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child: dataSnapshot.getChildren()){
                            String friendUid = child.getKey();
                            DatabaseReference reference = getContactsReference(friendUid, myUid);
                            Map<String, Object> updates = new HashMap<>();
                            updates.put(User.PHOTO_URL, photoUrl);
                            reference.updateChildren(updates);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onError(R.string.profile_error_imageUpdated);
                    }
                });
    }
}
