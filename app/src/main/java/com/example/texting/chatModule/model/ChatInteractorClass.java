package com.example.texting.chatModule.model;

import android.app.Activity;
import android.net.Uri;

import com.example.texting.chatModule.events.ChatEvent;
import com.example.texting.chatModule.model.dataAccess.RealtimeDatabase;
import com.example.texting.chatModule.model.dataAccess.Storage;
import com.example.texting.common.Constants;
import com.example.texting.common.model.StorageUploadImageCallback;
import com.example.texting.common.model.dataAccess.FirebaseAuthenticationAPI;
import com.example.texting.common.pojo.Message;
import com.example.texting.common.pojo.User;

import org.greenrobot.eventbus.EventBus;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 31/08/2020.
 * Derechos Reservados 2020
 */
public class ChatInteractorClass implements ChatInteractor {

    private RealtimeDatabase database;
    private FirebaseAuthenticationAPI authenticationAPI;
    private Storage storage;

    private User myUser;
    private String friendUid;
    private String friendEmail;

    private long lastConnectionFriend;
    private String uidConnectedFriend = "";

    public ChatInteractorClass() {
        this.database = new RealtimeDatabase();
        this.authenticationAPI = FirebaseAuthenticationAPI.getInstance();
        this.storage = new Storage();
    }

    private User getCurrentUser(){
        if (myUser == null){
            myUser = authenticationAPI.getAuthUser();
        }
        return myUser;
    }

    @Override
    public void subscribeToFriend(String friendUid, String friendEmail) {
        this.friendEmail = friendEmail;
        this.friendUid = friendUid;

        database.suscribeToFriend(friendUid, new LastConnectionEventListener() {
            @Override
            public void onSuccess(boolean online, long lastConnection, String uidConnectedFriendRes) {
                postStatusFriend(online, lastConnection);
                 uidConnectedFriend = uidConnectedFriendRes;
                 lastConnectionFriend = lastConnection;
            }
        });

        database.setMessageRead(getCurrentUser().getUid(), friendUid);
    }

    @Override
    public void unsubscribeToFriend(String friendUid) {
        database.unsuscribeToFriend(friendUid);
    }

    @Override
    public void subscribeToMessages() {
        database.suscribeToMessage(getCurrentUser().getEmail(), friendEmail, new MessageEventListener() {
            @Override
            public void onMessageReceived(Message message) {
                String msgSender = message.getSender();
                message.setSendByMe(msgSender.equals(getCurrentUser().getEmail()));
                postMessage(message);
            }

            @Override
            public void onError(int resMsg) {
                post(ChatEvent.ERROR_SERVER, resMsg);
            }
        });
        database.getDatabaseAPI().updateMyLastConnection(Constants.ONLINE, friendUid, getCurrentUser().getUid());
    }

    @Override
    public void unsubscribeToMessages() {
        database.unsubscribeToMessages(getCurrentUser().getEmail(), friendEmail);
        database.getDatabaseAPI().updateMyLastConnection(Constants.OFFLINE, getCurrentUser().getUid());
    }

    @Override
    public void sendMessage(String msg) {
        sendMessage(msg, null);
    }

    @Override
    public void sendImage(Activity activity, Uri imageUri) {
        storage.uploadImageChat(activity, imageUri, getCurrentUser().getEmail(), new StorageUploadImageCallback() {
            @Override
            public void onSuccess(Uri newUri) {
                sendMessage(null, newUri.toString());
                postUploadSuccess();
            }

            @Override
            public void onError(int resMsg) {
                post(ChatEvent.IMAGE_UPLOAD_FAIL, resMsg);
            }
        });
    }

    private void sendMessage(final String message, String photoUrl){
        database.sendMessage(message, photoUrl, friendEmail, getCurrentUser(),
                new SendMessageListener() {
                    @Override
                    public void onSuccess() {
                        if (!uidConnectedFriend.equals(getCurrentUser().getUid())){
                            database.sumUnreadMessages(getCurrentUser().getUid(), friendUid);

                            // TODO: 31/08/2020 notify
                            if (lastConnectionFriend != Constants.ONLINE_VALUE){

                            }
                        }
                    }
                });
    }

    private void postUploadSuccess() {
        post(ChatEvent.IMAGE_UPLOAD_SUCCESS, 0, null, false, 0);
    }

    private void postMessage(Message message){
        post(ChatEvent.MESSAGE_ADDED, 0, message, false, 0);
    }

    private void post(int typeEvent, int resMsg) {
        post(typeEvent, resMsg, null, false, 0);
    }

    private void postStatusFriend(boolean online, long lastConnection) {
        post(ChatEvent.GET_STATUS_FRIEND, 0, null, online, lastConnection);
    }

    private void post(int typeEvent, int resMsg, Message message, boolean online, long lastConnection) {
        ChatEvent event = new ChatEvent();
        event.setTypeEvent(typeEvent);
        event.setResMsg(resMsg);
        event.setMessage(message);
        event.setConnected(online);
        event.setLastConnection(lastConnection);

        EventBus.getDefault().post(event);
    }

}
