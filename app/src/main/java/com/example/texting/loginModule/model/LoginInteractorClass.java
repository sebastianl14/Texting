package com.example.texting.loginModule.model;

import com.example.texting.common.model.EventErrorTypeListener;
import com.example.texting.common.pojo.User;
import com.example.texting.loginModule.events.LoginEvent;
import com.example.texting.loginModule.model.dataAccess.Authentication;
import com.example.texting.loginModule.model.dataAccess.RealtimeDatabase;
import com.example.texting.loginModule.model.dataAccess.StatusAuthCallback;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;

public class LoginInteractorClass implements LoginInteractor {

    private Authentication authentication;
    private RealtimeDatabase database;

    public LoginInteractorClass() {
        authentication = new Authentication();
        database = new RealtimeDatabase();
    }

    @Override
    public void onResume() {
        authentication.onResume();
    }

    @Override
    public void onPause() {
        authentication.onPause();
    }

    @Override
    public void getStatusAuth() {
        authentication.getStatusAuth(new StatusAuthCallback() {
            @Override
            public void onGetUser(FirebaseUser user) {
                post(LoginEvent.STATUS_AUTH_SUCCESS, user);

                database.checkUserExist(authentication.getCurrentUser().getUid(), new EventErrorTypeListener() {
                    @Override
                    public void onError(int typeEvent, int rsgMsg) {
                        if (typeEvent == LoginEvent.USER_NOT_EXIST){
                            registerUser();
                        } else {
                            post(typeEvent);
                        }
                    }
                });
            }

            @Override
            public void onLauchUILogin() {
                post(LoginEvent.STATUS_AUTH_ERROR);
            }
        });
    }

    private void registerUser() {
        User currentUser = authentication.getCurrentUser();
        database.registerUser(currentUser);
    }

    private void post(int typeEvent) {
        post(typeEvent, null);
    }

    private void post(int typeEvent, FirebaseUser user) {
        LoginEvent event = new LoginEvent();
        event.setTypeEvent(typeEvent);
        event.setUser(user);

        EventBus.getDefault().post(event);
    }
}
