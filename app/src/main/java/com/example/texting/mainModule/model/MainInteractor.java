package com.example.texting.mainModule.model;

import com.example.texting.common.pojo.User;

public interface MainInteractor {

    void subscribeToUserList();
    void unsubscribeToUserList();

    void signOff();

    User getCurrentUser();
    void removedFriend(String friendUid);

    void acceptRequest(User user);
    void denyRequest(User user);
}
