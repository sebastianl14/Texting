package com.example.texting.common.model.dataAccess;

import com.example.texting.common.utils.UtilsCommon;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 22/08/2020.
 * Derechos Reservados 2020
 */
public class FirebaseStorageAPI {

    private FirebaseStorage firebaseStorage;

    private static class Singleton{

        private static final FirebaseStorageAPI INSTANCE = new FirebaseStorageAPI();
    }

    public static FirebaseStorageAPI getInstance(){
        return Singleton.INSTANCE;
    }

    private FirebaseStorageAPI(){
        this.firebaseStorage = FirebaseStorage.getInstance();
    }

    public FirebaseStorage getFirebaseStorage() {
        return firebaseStorage;
    }

    public StorageReference getPhotosReferenceByEmail(String email){
        return firebaseStorage.getReference().child(UtilsCommon.getEmailEncoded(email));
    }
}
