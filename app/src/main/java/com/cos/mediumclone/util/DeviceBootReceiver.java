package com.cos.mediumclone.util;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.cos.mediumclone.view.activity.auth.LoginActivity;

public class DeviceBootReceiver extends BroadcastReceiver {
    
    private static final String TAG = "DeviceBootReceiverTest";
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "부트리시버 실행", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onReceive: ");

        if (intent != null) {
            String action = intent.getAction();
            if (action != null && action.equals(Intent.ACTION_BOOT_COMPLETED)){
                Log.d(TAG, "onReceive: ACTION_BOOT_COMPLETED");
                try {
                    Intent appIntent = new Intent(context, LoginActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                        try {
                            pendingIntent.send();
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                    } else {
                        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(appIntent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "exception: " + e.getMessage());
                }

            }
        }

    }
}
