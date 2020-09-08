package com.example.texting.mainModule.model;

import com.example.texting.common.Constants;
import com.example.texting.common.model.BasicEventCallback;
import com.example.texting.common.model.dataAccess.FirebaseCloudMessagingAPI;
import com.example.texting.common.pojo.User;
import com.example.texting.mainModule.events.MainEvent;
import com.example.texting.mainModule.model.dataAccess.Authentication;
import com.example.texting.mainModule.model.dataAccess.RealtimeDatabase;
import com.example.texting.mainModule.model.dataAccess.UserEventListener;

import org.greenrobot.eventbus.EventBus;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 15/08/2020.
 * Derechos Reservados 2020
 */
public class MainInteractorClass implements MainInteractor {

    private RealtimeDatabase database;
    private Authentication authentication;
    //notify
    private FirebaseCloudMessagingAPI cloudMessagingAPI;


    private User myUser = null;

    public MainInteractorClass() {
        database = new RealtimeDatabase();
        authentication = new Authentication();
        //notify
        cloudMessagingAPI = FirebaseCloudMessagingAPI.getInstance();
    }

    @Override
    public void subscribeToUserList() {
        database.subscribeToUserList(getCurrentUser().getUid(), new UserEventListener() {
            @Override
            public void onUserAdded(User user) {
                post(MainEvent.USER_ADDED, user);
            }

            @Override
            public void onUserUpdated(User user) {
                post(MainEvent.USER_UPDATED, user);
            }

            @Override
            public void onUserRemoverd(User user) {
                post(MainEvent.USER_REMOVED, user);
            }

            @Override
            public void onError(int resMsg) {
                postError(resMsg);
            }
        });

        database.subscribeToRequests(getCurrentUser().getEmail(), new UserEventListener() {
            @Override
            public void onUserAdded(User user) {
                post(MainEvent.REQUEST_ADDED, user);
            }

            @Override
            public void onUserUpdated(User user) {
                post(MainEvent.REQUEST_UPDATED, user);
            }

            @Override
            public void onUserRemoverd(User user) {
                post(MainEvent.REQUEST_REMOVED, user);
            }

            @Override
            public void onError(int resMsg) {
                post(MainEvent.ERROR_SERVER);
            }
        });

        changeConnectionStatus(Constants.ONLINE);
    }

    private void changeConnectionStatus(boolean online) {
        database.getDatabaseAPI().updateMyLastConnection(online, getCurrentUser().getUid());
    }

    @Override
    public void unsubscribeToUserList() {
        database.unsubscribeToUser(getCurrentUser().getUid());
        database.unsubscribeToRequest(getCurrentUser().getEmail());

        changeConnectionStatus(Constants.OFFLINE);
    }

    @Override
    public void signOff() {
        cloudMessagingAPI.unsuscribeToMyTopic(getCurrentUser().getEmail());

        authentication.signOff();
    }

    @Override
    public User getCurrentUser() {
        return myUser == null ? authentication.getAuthenticationAPI().getAuthUser() : myUser;
    }

    @Override
    public void removedFriend(String friendUid) {
        database.removeUser(friendUid, getCurrentUser().getUid(), new BasicEventCallback() {
            @Override
            public void onSuccess() {
                post(MainEvent.USER_REMOVED);
            }

            @Override
            public void onError() {
                post(MainEvent.ERROR_SERVER);
            }
        });
    }

    @Override
    public void acceptRequest(User user) {
        database.acceptRequest(user, getCurrentUser(), new BasicEventCallback() {
            @Override
            public void onSuccess() {
                post(MainEvent.REQUEST_ACCEPTED, user);
            }

            @Override
            public void onError() {
                post(MainEvent.ERROR_SERVER);
            }
        });
    }

    @Override
    public void denyRequest(User user) {
        database.denyRequest(user, getCurrentUser().getEmail(), new BasicEventCallback() {
            @Override
            public void onSuccess() {
                post(MainEvent.REQUEST_DENY, user);
            }

            @Override
            public void onError() {
                post(MainEvent.ERROR_SERVER);
            }
        });
    }

    private void post(int typeEvent) {
        post(typeEvent, null, 0);
    }

    private void postError(int resMsg) {
        post(MainEvent.ERROR_SERVER, null, resMsg);
    }

    private void post(int typeEvent, User user){
        post(typeEvent, user, 0);
    }

    private void post(int typeEvent, User user, int resMsg) {
        MainEvent event = new MainEvent();
        event.setTypeEvent(typeEvent);
        event.setUser(user);
        event.setResMsg(resMsg);

        EventBus.getDefault().post(event);
    }
}
