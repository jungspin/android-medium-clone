package com.cos.mediumclone.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.cos.mediumclone.databinding.ItemCustomDialogTwoBinding;

public class CustomDialog extends DialogFragment {
    private static final String TAG = "CustomDialog";

    private static AlertDialog alertDialog;
    public static CustomDialog getInstance() {
        CustomDialog fragment = new CustomDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void showDialog(Context context, String title, String message, CustomDialogAction customDialogAction) {
        dismissDialog();

        LayoutInflater inflater = LayoutInflater.from(context);
        ItemCustomDialogTwoBinding confirmDialogBinding = ItemCustomDialogTwoBinding.inflate(inflater);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(confirmDialogBinding.getRoot());
        alertDialogBuilder.setCancelable(false);

        alertDialog = alertDialogBuilder.create();

        confirmDialogBinding.mDialogTitle.setText(title);
        confirmDialogBinding.mDialogContent.setText(message);
        confirmDialogBinding.mDialogBtnYes.setOnClickListener(v -> {
            customDialogAction.setPositiveAction();
            dismissDialog();
        });
        confirmDialogBinding.mDialogBtnNo.setOnClickListener(v -> {
            customDialogAction.setNegativeAction();
            dismissDialog();
        });

        if (alertDialog != null) {
            alertDialog.show();

            Window dialogWindow = alertDialog.getWindow();
            if (dialogWindow != null) {
                WindowManager.LayoutParams attribute = alertDialog.getWindow().getAttributes();
                attribute.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.65);
                attribute.height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.45);

                alertDialog.getWindow().setAttributes(attribute);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ViewGroup.LayoutParams layoutParams = confirmDialogBinding.getRoot().getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

                confirmDialogBinding.getRoot().setLayoutParams(layoutParams);
            }
        }
    }

    public void dismissDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    public interface CustomDialogAction {
        void setPositiveAction();
        void setNegativeAction();
    }

}
