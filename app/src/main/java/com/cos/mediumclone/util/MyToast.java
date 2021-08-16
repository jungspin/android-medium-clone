package com.cos.mediumclone.util;

import android.content.Context;
import android.widget.Toast;

public class MyToast {

    public static void toast(Context mContext, CharSequence text){

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(mContext, text, duration);
        toast.show();
    }
}
