package com.example.texting.addModule.model;

import com.example.texting.addModule.events.AddEvent;
import com.example.texting.addModule.model.dataAccess.RealtimeDatebase;
import com.example.texting.common.model.BasicEventCallback;
import com.example.texting.common.model.dataAccess.FirebaseAuthenticationAPI;

import org.greenrobot.eventbus.EventBus;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 22/08/2020.
 * Derechos Reservados 2020
 */
public class AddInteractorClass implements AddInteractor {

    private RealtimeDatebase datebase;
    private FirebaseAuthenticationAPI authenticationAPI;

    public AddInteractorClass() {
        datebase = new RealtimeDatebase();
        authenticationAPI = FirebaseAuthenticationAPI.getInstance();
    }

    @Override
    public void addFriend(String email) {
        datebase.addFriend(email, authenticationAPI.getAuthUser(), new BasicEventCallback() {
            @Override
            public void onSuccess() {
                post(AddEvent.SEND_REQUEST_SUCCESS);
            }

            @Override
            public void onError() {
                post(AddEvent.ERROR_SERVER);
            }
        });
    }

    private void post(int typeEvent) {
        AddEvent event = new AddEvent();
        event.setTypeEvent(typeEvent);

        EventBus.getDefault().post(event);
    }
}
