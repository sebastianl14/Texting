package com.example.texting.common.model.dataAccess;

import com.example.texting.common.Constants;
import com.example.texting.common.pojo.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class FirebaseRealtimeDatabaseAPI {

    public static final String SEPARARTOR = "__&__";
    public static final String PATH_USERS = "users";
    public static final String PATH_CONTACTS = "contacts";
    public static final String PATH_REQUEST = "requests";

    private DatabaseReference databaseReference;

    private static class SingletonHolder{
        private static final FirebaseRealtimeDatabaseAPI INSTANCE = new FirebaseRealtimeDatabaseAPI();
    }

    public static FirebaseRealtimeDatabaseAPI getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private FirebaseRealtimeDatabaseAPI(){
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    /*
    * References
     */
    public DatabaseReference getRootReference(){
        return databaseReference.getRoot();
    }

    public DatabaseReference getUserReferenceByUid(String uid){
        return getRootReference().child(PATH_USERS).child(uid);
    }

    public DatabaseReference getContactsReference(String myUid) {
        return getUserReferenceByUid(myUid).child(PATH_CONTACTS);
    }

    public DatabaseReference getRequestReference(String email) {
        return getRootReference().child(PATH_REQUEST).child(email);
    }

    public void updateMyLastConnection(boolean online, String uid) {
        updateMyLastConnection(online, "", uid);
    }

    public void updateMyLastConnection(boolean online, String uidFriend, String uid) {
        String lastConnectionWith = Constants.ONLINE_VALUE + SEPARARTOR + uidFriend;

        Map<String, Object> values = new HashMap<>();
        values.put(User.LAST_CONNECTION_WITH, online ? lastConnectionWith : ServerValue.TIMESTAMP);
        //offline
        getUserReferenceByUid(uid).child(User.LAST_CONNECTION_WITH).keepSynced(true);
        getUserReferenceByUid(uid).updateChildren(values);

        if (online){
            getUserReferenceByUid(uid).child(User.LAST_CONNECTION_WITH).onDisconnect()
                    .setValue(ServerValue.TIMESTAMP);
        } else {
            getUserReferenceByUid(uid).child(User.LAST_CONNECTION_WITH).onDisconnect().cancel();
        }
    }

}
