package com.example.texting.chatModule.model;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.example.texting.chatModule.ChatPresenter;
import com.example.texting.chatModule.events.ChatEvent;
import com.example.texting.chatModule.view.ChatView;
import com.example.texting.common.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 31/08/2020.
 * Derechos Reservados 2020
 */
public class ChatPresenterClass implements ChatPresenter {

    private ChatView view;
    private ChatInteractor interactor;

    private String friendUid, friendEmail;

    public ChatPresenterClass(ChatView view) {
        this.view = view;
        this.interactor = new ChatInteractorClass();
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        view = null;
    }

    @Override
    public void onPause() {
        if (view != null) {
            interactor.unsubscribeToFriend(friendUid);
            interactor.unsubscribeToMessages();
        }
    }

    @Override
    public void onResume() {
        if (view != null){
            interactor.subscribeToFriend(friendUid, friendEmail);
            interactor.subscribeToMessages();
        }
    }

    @Override
    public void setupFriend(String uid, String email) {
        friendEmail = email;
        friendUid = uid;
    }

    @Override
    public void sendMessage(String msg) {
        if (view != null){
            interactor.sendMessage(msg);
        }
    }

    @Override
    public void sendImage(Activity activity, Uri imageUri) {
        if (view != null){
            view.showProgrees();
            interactor.sendImage(activity, imageUri);
        }
    }

    @Override
    public void result(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RC_PHOTO_PICKER && resultCode == Activity.RESULT_OK){
            view.openDialogPreview(data);
        }
    }

    @Subscribe
    @Override
    public void onEventListener(ChatEvent event) {
        if (view != null){
            switch (event.getTypeEvent()){
                case ChatEvent.MESSAGE_ADDED:
                    view.onMessageReceived(event.getMessage());
                    break;
                case ChatEvent.IMAGE_UPLOAD_SUCCESS:
                    view.hideProgress();
                    break;
                case ChatEvent.GET_STATUS_FRIEND:
                    view.onStatusUser(event.isConnected(), event.getLastConnection());
                    break;
                case ChatEvent.ERROR_SERVER:
                case ChatEvent.ERROR_VOLEY:
                case ChatEvent.IMAGE_UPLOAD_FAIL:
                case ChatEvent.ERROR_PROCESS_DATA:
                    view.hideProgress();
                    view.onError(event.getResMsg());
                    break;
            }
        }
    }
}
