package com.example.texting.common.model;

import android.net.Uri;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 22/08/2020.
 * Derechos Reservados 2020
 */
public interface StorageUploadImageCallback {

    void onSuccess(Uri newUri);
    void onError(int resMsg);
}
