package com.example.texting.addModule;

import com.example.texting.addModule.events.AddEvent;
import com.example.texting.addModule.model.AddInteractor;
import com.example.texting.addModule.model.AddInteractorClass;
import com.example.texting.addModule.view.AddView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 22/08/2020.
 * Derechos Reservados 2020
 */
public class AddPresenterClass implements AddPresenter {

    private AddView view;
    private AddInteractor interactor;

    public AddPresenterClass(AddView view) {
        this.view = view;
        this.interactor = new AddInteractorClass();
    }

    @Override
    public void onShow() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        view = null;
    }

    @Override
    public void addFriend(String email) {
        if (view != null){
            view.disableUIElements();
            view.showProgress();

            interactor.addFriend(email);
        }
    }

    @Subscribe
    @Override
    public void onEventListener(AddEvent event) {
        if (view != null){
            view.hideProgress();
            view.enableUIElements();

            switch (event.getTypeEvent()){
                case AddEvent.SEND_REQUEST_SUCCESS:
                    view.friendAdded();
                    break;
                case AddEvent.ERROR_SERVER:
                    view.friendNotAdded();
                    break;
                case AddEvent.ERROR_EXIST:
                    view.showMessageExist(event.getResMsg());
                    break;
            }
        }
    }
}
