package com.example.texting.common.model.dataAccess;

import androidx.annotation.NonNull;

import com.example.texting.common.utils.UtilsCommon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 4/09/2020.
 * Derechos Reservados 2020
 */
public class FirebaseCloudMessagingAPI {

    private FirebaseMessaging firebaseMessaging;

    private static class SingletonHolder{
        private static final FirebaseCloudMessagingAPI INSTANCE = new FirebaseCloudMessagingAPI();
    }

    public static FirebaseCloudMessagingAPI getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public FirebaseCloudMessagingAPI() {
        this.firebaseMessaging = FirebaseMessaging.getInstance();
    }

    //Methods

    public void suscribeToMyTopic(String myEmail){
        firebaseMessaging.subscribeToTopic(UtilsCommon.getEmailToTopic(myEmail))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()){
                            //reintentar y notificar
                        }
                    }
                });
    }

    public void unsuscribeToMyTopic(String myEmail){
        firebaseMessaging.unsubscribeFromTopic(UtilsCommon.getEmailToTopic(myEmail))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()){
                            //reintentar
                        }
                    }
                });
    }

}
