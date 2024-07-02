package com.cos.mediumclone.helper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PermissionHelper {
    // 사용자 권한 목록
    private List<String> permissions;

    private static final int REQUEST_PERMISSION_CODE = 1000;

    public PermissionHelper() {
        permissions = getPermissions();
    }

    /**
     * 버전별 권한 부여가 필요한 목록 반환
     * minSdk : 24 (N) / target : 33 (T)
     *
     * @return 사용자 권한 목록
     */
    private List<String> getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions = Arrays.asList(
                    Manifest.permission.SYSTEM_ALERT_WINDOW
            );
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions = Arrays.asList(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            );
        } else {
            permissions = Arrays.asList(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            );
        }

        return permissions;
    }

    /**
     * 권한 체크 메서드
     *
     * @param activity                activity
     * @param context                 context
     * @param permissionArray         권한 목록. null 일 경우 모든 권한 확인
     * @param requestPermissionResult 권한 부여 결과 인터페이스
     */
    public void checkPermissions(AppCompatActivity activity,
                                 Context context,
                                 List<String> permissionArray,
                                 RequestPermissionResult requestPermissionResult) {
        List<String> rejectedPermissions = new ArrayList<>();
        List<String> requirePermissions;
        if (permissionArray == null) {
            requirePermissions = permissions;
        } else {
            requirePermissions = permissionArray;
        }

        for (String permission : requirePermissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                rejectedPermissions.add(permission);
            }
        }

        if (rejectedPermissions.isEmpty()) {
            // 권한이 모두 부여된 경우
            // 다음 행위
            requestPermissionResult.onGranted();
        } else {
            // 부여되지 않은 권한이 있는 경우
            int rejectedPermissionsSize = rejectedPermissions.size();
            String[] rejectPermissionsArray = rejectedPermissions.toArray(new String[rejectedPermissionsSize]);
            // 권한 요청
            launchRequestPermission(activity, requestPermissionResult, rejectPermissionsArray);
        }
    }

    /**
     * 권한 요청
     */
    private void launchRequestPermission(AppCompatActivity activity,
                                         RequestPermissionResult requestPermissionResult,
                                         String[] rejectPermissionsArray) {

        List<String> deniedPermissions = new ArrayList<>();

        ActivityResultLauncher<String[]> requestPermissionLauncher = (
                activity.registerForActivityResult(
                        new ActivityResultContracts.RequestMultiplePermissions(),
                        resultMap -> {
                            for (Map.Entry<String, Boolean> result : resultMap.entrySet()) {
                                String deniedPermission = result.getKey();
                                if (!result.getValue()) {
                                    deniedPermissions.add(deniedPermission);
                                }
                            }

                            if (deniedPermissions.isEmpty()) {
                                requestPermissionResult.onGranted();
                            } else {
                                // TODO: 2024-06-10  이후 동작 생각하기
                                new AlertDialog.Builder(activity)
                                        .setMessage("원활한 앱 사용을 위해 권한이 필요합니다.")
                                        .setPositiveButton("확인", (dialog, which) -> {
                                            // 앱 권한 설정으로 이동
                                            Log.d("PERMISSION_TEST", "launchRequestPermission: 확인");
                                            Intent i = new Intent();
                                            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            i.setData(Uri.fromParts("package", activity.getPackageName(), null));
                                            activity.startActivity(i);
                                            dialog.dismiss();
                                        })
                                        .setNegativeButton("취소", (dialog, which) -> {
                                            Log.d("PERMISSION_TEST", "launchRequestPermission: 취소");
                                            requestPermissionResult.onDenied();
                                        })
                                        .show();

                            }
                        }
                )
        );

        requestPermissionLauncher.launch(rejectPermissionsArray);

        if (rejectPermissionsArray.length > 0) {
            ActivityCompat.requestPermissions(activity, rejectPermissionsArray, REQUEST_PERMISSION_CODE);
        }

    }


    public interface RequestPermissionResult {
        void onGranted();

        void onDenied();
    }

}
