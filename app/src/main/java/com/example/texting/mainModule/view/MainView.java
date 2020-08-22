package com.example.texting.mainModule.view;

import com.example.texting.common.pojo.User;

public interface MainView {

    void friendAdded(User user);
    void friendUpdated(User user);
    void friendRemoved(User user);

    void requestAdded(User user);
    void requestUpdated(User user);
    void requestRemoved(User user);

    void showRequestAccepted(String userName);
    void showRequestDenied();

    void showFriendRemoved();

    void showError(int rsgMsg);
}
