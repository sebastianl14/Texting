package com.example.texting.chatModule.model.dataAccess;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.texting.R;
import com.example.texting.chatModule.model.LastConnectionEventListener;
import com.example.texting.chatModule.model.MessageEventListener;
import com.example.texting.chatModule.model.SendMessageListener;
import com.example.texting.common.Constants;
import com.example.texting.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.example.texting.common.pojo.Message;
import com.example.texting.common.pojo.User;
import com.example.texting.common.utils.UtilsCommon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 31/08/2020.
 * Derechos Reservados 2020
 */
public class RealtimeDatabase {

    private static final String PATH_CHATS = "chats";
    private static final String PATH_MESSAGE = "message";

    private FirebaseRealtimeDatabaseAPI databaseAPI;

    private ChildEventListener messageEventListener;
    private ValueEventListener friendProfileListener;

    public RealtimeDatabase() {
        databaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
    }

    public FirebaseRealtimeDatabaseAPI getDatabaseAPI() {
        return databaseAPI;
    }

    public void suscribeToMessage(String myEmail, String friendEmail, MessageEventListener listener){
        if (messageEventListener == null){
            messageEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    listener.onMessageReceived(getMessage(dataSnapshot));
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    switch (databaseError.getCode()){
                        case DatabaseError.PERMISSION_DENIED:
                            listener.onError(R.string.chat_error_permission_denied);
                            break;
                        default:
                            listener.onError(R.string.common_error_server);
                            break;
                    }
                }
            };
        }

        getChatsMessagesReference(myEmail, friendEmail).addChildEventListener(messageEventListener);
    }

    private DatabaseReference getChatsMessagesReference(String myEmail, String friendEmail) {
        return getChatsReference(myEmail, friendEmail).child(PATH_MESSAGE);
    }

    private DatabaseReference getChatsReference(String myEmail, String friendEmail) {
        String myEmailEncoded = UtilsCommon.getEmailEncoded(myEmail);
        String frindEmailEncoded = UtilsCommon.getEmailEncoded(friendEmail);

        String keyChat = myEmailEncoded + FirebaseRealtimeDatabaseAPI.SEPARARTOR + frindEmailEncoded;
        if (myEmailEncoded.compareTo(frindEmailEncoded) > 0){
            keyChat = frindEmailEncoded + FirebaseRealtimeDatabaseAPI.SEPARARTOR + myEmailEncoded;
        }

        return databaseAPI.getRootReference().child(PATH_CHATS).child(keyChat);
    }

    private Message getMessage(DataSnapshot dataSnapshot) {
        Message message = dataSnapshot.getValue(Message.class);
        if (message != null){
            message.setUid(dataSnapshot.getKey());
        }
        return message;
    }

    public void unsubscribeToMessages(String myEmail, String friendEmail){
        if (messageEventListener != null){
            getChatsMessagesReference(myEmail, friendEmail).removeEventListener(messageEventListener);
        }
    }

    public void suscribeToFriend(String uid, LastConnectionEventListener listener){
        if (friendProfileListener == null){
            friendProfileListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long lastConnectionFriend = 0;
                    String uidConnectedFriend  = "";

                    try {
                        Long value = dataSnapshot.getValue(Long.class);
                        if (value != null){
                            lastConnectionFriend = value;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        String lastConnectionWith = dataSnapshot.getValue(String.class);
                        if (lastConnectionWith != null && !lastConnectionWith.isEmpty()){
                            String[] values = lastConnectionWith.split(FirebaseRealtimeDatabaseAPI.SEPARARTOR);
                            if (values.length > 0){
                                lastConnectionFriend = Long.valueOf(values[0]);
                                if (values.length > 1){
                                    uidConnectedFriend = values[1];
                                }
                            }
                        }
                    }

                    listener.onSuccess(lastConnectionFriend == Constants.ONLINE_VALUE, lastConnectionFriend,
                            uidConnectedFriend);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
        }

        //offline Siempre se sincronice con el Servidor
        databaseAPI.getUserReferenceByUid(uid).child(User.LAST_CONNECTION_WITH).keepSynced(true);
        databaseAPI.getUserReferenceByUid(uid).child(User.LAST_CONNECTION_WITH)
                .addValueEventListener(friendProfileListener);
    }


    public void unsuscribeToFriend(String uid){
        if (friendProfileListener != null){
            databaseAPI.getUserReferenceByUid(uid).child(User.LAST_CONNECTION_WITH)
                    .removeEventListener(friendProfileListener);
        }
    }

    /*
     * read/unread messages
     * @param myUid
     * @param friendUid
     */
    public void setMessageRead(String myUid, String friendUid){
        final DatabaseReference userReference = getOneContactReference(myUid, friendUid);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null){
                    Map<String, Object> updates = new HashMap<>();
                    updates.put(User.MESSAGE_UNREAD, 0);
                    userReference.updateChildren(updates);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private DatabaseReference getOneContactReference(String uidMain, String childUid) {
        return databaseAPI.getUserReferenceByUid(uidMain)
                .child(FirebaseRealtimeDatabaseAPI.PATH_CONTACTS).child(childUid);
    }

    public void sumUnreadMessages(String myUid, String friendUid){
        final DatabaseReference userReference = getOneContactReference(friendUid, myUid);

        userReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                User user = mutableData.getValue(User.class);
                if (user == null){
                    return Transaction.success(mutableData);
                }

                user.setMessageUnread(user.getMessageUnread() + 1);
                mutableData.setValue(user);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }

    /**
     * Send Message
     * @param msg
     * @param photoUrl
     * @param friendEmail
     * @param myUser
     * @param listener
     */
    public void sendMessage(String msg, String photoUrl, String friendEmail, User myUser,
                            SendMessageListener listener){
        Message message = new Message();
        message.setSender(myUser.getEmail());
        message.setMsg(msg);
        message.setPhotoUrl(photoUrl);

        DatabaseReference chatReference = getChatsMessagesReference(myUser.getEmail(), friendEmail);
        chatReference.push().setValue(message, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null){
                    listener.onSuccess();
                }
            }
        });
    }
}
