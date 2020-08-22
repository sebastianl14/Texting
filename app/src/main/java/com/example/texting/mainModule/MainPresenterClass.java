package com.example.texting.mainModule;

import com.example.texting.common.pojo.User;
import com.example.texting.mainModule.events.MainEvent;
import com.example.texting.mainModule.model.MainInteractor;
import com.example.texting.mainModule.model.MainInteractorClass;
import com.example.texting.mainModule.view.MainView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 15/08/2020.
 * Derechos Reservados 2020
 */
public class MainPresenterClass implements MainPresenter {

    private MainView view;
    private MainInteractor interactor;

    public MainPresenterClass(MainView view) {
        this.view = view;
        this.interactor = new MainInteractorClass();
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
        if (view != null){
            interactor.unsubscribeToUserList();
        }
    }

    @Override
    public void onResume() {
        if (view != null){
            interactor.subscribeToUserList();
        }
    }

    @Override
    public void signOff() {
        interactor.unsubscribeToUserList();
        interactor.signOff();
        onDestroy();
    }

    @Override
    public User getCurrentUser() {
        return interactor.getCurrentUser();
    }

    @Override
    public void removeFriend(String friendUid) {
        if (view != null){
            interactor.removedFriend(friendUid);
        }
    }

    @Override
    public void acceptRequest(User user) {
        if (view != null){
            interactor.acceptRequest(user);
        }
    }

    @Override
    public void denyRequest(User user) {
        if (view != null){
            interactor.denyRequest(user);
        }
    }

    @Subscribe
    @Override
    public void onEventListener(MainEvent event) {
        if (view != null){
            User user = event.getUser();

            switch (event.getTypeEvent()){
                case MainEvent.USER_ADDED:
                    view.friendAdded(user);
                    break;
                case MainEvent.USER_UPDATED:
                    view.friendUpdated(user);
                    break;
                case MainEvent.USER_REMOVED:
                    if (user != null) {
                        view.friendRemoved(user);
                    } else {
                        view.showFriendRemoved();
                    }
                    break;
                case MainEvent.REQUEST_ADDED:
                    view.requestAdded(user);
                    break;
                case MainEvent.REQUEST_UPDATED:
                    view.requestUpdated(user);
                    break;
                case MainEvent.REQUEST_REMOVED:
                    view.requestRemoved(user);
                    break;
                case MainEvent.REQUEST_ACCEPTED:
                    view.showRequestAccepted(user.getUsername());
                    break;
                case MainEvent.REQUEST_DENY:
                    view.showRequestDenied();
                    break;
                case MainEvent.ERROR_SERVER:
                    view.showError(event.getResMsg());
                    break;
            }
        }
    }
}
