package com.example.texting.loginModule;

import android.app.Activity;
import android.content.Intent;

import com.example.texting.R;
import com.example.texting.loginModule.events.LoginEvent;
import com.example.texting.loginModule.model.LoginInteractor;
import com.example.texting.loginModule.model.LoginInteractorClass;
import com.example.texting.loginModule.view.LoginActivity;
import com.example.texting.loginModule.view.LoginView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LoginPresenterClass implements LoginPresenter {

    private LoginView view;
    private LoginInteractor interactor;


    public LoginPresenterClass(LoginView view) {
        this.view = view;
        this.interactor = new LoginInteractorClass();
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        if (setProgress()) {
            interactor.onResume();
        }
    }

    private boolean setProgress() {
        if (view != null){
            view.showProgress();
            return true;
        }
        return false;
    }

    @Override
    public void onPause() {
        if (setProgress()){
            interactor.onPause();
        }
    }

    @Override
    public void onDestroy() {
        view = null;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void result(int requestCode, int resultCode, Intent data) {
        if (requestCode == Activity.RESULT_OK){

            switch (requestCode){
                case LoginActivity.RC_SIGN_IN:
                    if (data != null){
                        view.showLoginSuccessfully(data);
                    }
                    break;
            }
        } else {
            view.showError(R.string.login_message_error);
        }
    }

    @Override
    public void getStatusAuth() {
        if (setProgress()){
            interactor.getStatusAuth();
        }
    }

    @Subscribe
    @Override
    public void onEventListener(LoginEvent event) {

        if (view != null){
            view.hideProgress();

            switch (event.getTypeEvent()){
                case LoginEvent.STATUS_AUTH_SUCCESS:
                    if (setProgress()){
                        view.showMessageStarting();
                        view.openMainActivity();
                    }
                    break;
                case LoginEvent.STATUS_AUTH_ERROR:
                    view.openUILogin();
                    break;
                case LoginEvent.ERROR_SERVER:
                    view.showError(event.getRsgMsg());
                    break;
            }
        }
    }
}
