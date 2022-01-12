package com.cos.mediumclone.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.viewbinding.ViewBinding;

import com.cos.mediumclone.databinding.ItemCustomDialogSingleBinding;
import com.cos.mediumclone.databinding.ItemCustomDialogTwoBinding;

public class CustomDialog extends DialogFragment {
    private static final String TAG = "CustomDialog";

    private AlertDialog alertDialog;
    private Context mContext;
    private @NonNull ItemCustomDialogSingleBinding bindingSingle;
    private @NonNull ItemCustomDialogTwoBinding bindingTwo;
    private View view;




    public CustomDialog(Context mContext) {
        this.mContext = mContext;
    }

    public interface CustomAction{
        void setPositiveAction();
    }




    public void showAlertDialog(boolean isTwoBtn, String title, String content, CustomAction customAction) {

        try {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        final String TITLE = String.valueOf(title);
        final String CONTENT = String.valueOf(content);

        LayoutInflater inflater = LayoutInflater.from(mContext);

        if (isTwoBtn) { // single
            bindingSingle = ItemCustomDialogSingleBinding.inflate(inflater);
            view = bindingSingle.getRoot();

            bindingSingle.mDialogTitle.setText(TITLE);
            bindingSingle.mDialogContent.setText(CONTENT);
            bindingSingle.mDialogSingleBtnConfirm.setOnClickListener(v->{
                customAction.setPositiveAction();
            });

        } else {
            bindingTwo = ItemCustomDialogTwoBinding.inflate(inflater);
            view = bindingTwo.getRoot();

            // 타이틀 및 내용 표시

            bindingTwo.mDialogTitle.setText(TITLE);
            bindingTwo.mDialogContent.setText(CONTENT);
            bindingTwo.mDialogBtnNo.setOnClickListener(v -> {
                alertDialog.dismiss();
            });
            bindingTwo.mDialogBtnYes.setOnClickListener(v -> {
                customAction.setPositiveAction();
                alertDialog.dismiss();
            });

        }


        builder.setView(view);
        builder.setCancelable(false);


        alertDialog = builder.create();
        try {
            alertDialog.show();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
