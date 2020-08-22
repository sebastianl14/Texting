package com.example.texting.common.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.texting.mainModule.view.MainActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Creado por Sebastian Londoño Benitez
 * Email: sebastianl14@hotmail.com
 * Fecha 15/08/2020.
 * Derechos Reservados 2020
 */
public class UtilsCommon {

    /*
     * Codificar un correo electrónico
     */

    public static String getEmailEncoded(String email){
        String preKey = email.replace("_", "__");
        return preKey.replace(".","_");
    }


    /**
     * Cargar Imagenes Basicas
     * @param context
     * @param url
     * @param target
     */
    public static void loadImage(Context context, String url, ImageView target) {

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(target);
    }
}
