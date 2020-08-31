package com.example.texting.common.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.texting.R;
import com.example.texting.mainModule.view.MainActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

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

    public static boolean validateEmail(Context context, EditText etEmail) {
        boolean isValid = true;

        String email = etEmail.getText().toString().trim();

        if (email.isEmpty()){
            etEmail.setError(context.getString(R.string.common_validate_field_required));
            etEmail.requestFocus();
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError(context.getString(R.string.common_validate_email_invalid));
            etEmail.requestFocus();
            isValid = false;
        }

        return isValid;
    }

    /**
     * Verificación de versión
     * @return
     */
    public static boolean hasMaterialDesign() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Mostrar mensajes con Snackbar.
     * @param contentMain
     * @param resMsg
     */
    public static void showSnackbar(View contentMain, int resMsg) {
        showSnackbar(contentMain, resMsg, Snackbar.LENGTH_SHORT);
    }

    public static void showSnackbar(View contentMain, int resMsg, int duration) {
        Snackbar.make(contentMain, resMsg, duration).show();
    }
}
