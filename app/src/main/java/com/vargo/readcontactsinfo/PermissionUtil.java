package com.vargo.readcontactsinfo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by zhanghongbiao@vargo.com.cn on 18-4-11.
 */

public class PermissionUtil {

    public static final int PERMISSIONS_CODE = 1;

    public static boolean isGrantReadContactsPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return true;
    }

    public static void reqestReadContactsPermission(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS
            }, PERMISSIONS_CODE);
        }
    }

}
