package win.grishanya.narsoe.permissions;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionChecer {
    private static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;
    private static int ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS_REQUEST_CODE = 5470;

    private Activity activity;
    private Context context;
    private CheckPermissions checkPermissions;

    private interface CheckPermissions{
        void OnPermissionApplied();
    }

    public PermissionChecer (Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    public void checkPermissions(){
        checkPermissions = new CheckPermissions() {
            @Override
            public void OnPermissionApplied() {
                checkToughPermissions();
            }
        };
        checkLightPermissions();

    }

    private void checkLightPermissions(){
        List<String> listOfNecessaryPermission = new ArrayList<String>();

        if (activity.checkSelfPermission(android.Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            listOfNecessaryPermission.add(Manifest.permission.READ_CALL_LOG);
        }
        //ToDo Проверить если необходимость в пермишене!
        if (activity.checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            listOfNecessaryPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (activity.checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            listOfNecessaryPermission.add(Manifest.permission.CALL_PHONE);
        }

        if(!listOfNecessaryPermission.isEmpty()){
            String [] arrayOfNecessaryPermission = new String[listOfNecessaryPermission.size()];
            listOfNecessaryPermission.toArray(arrayOfNecessaryPermission);
            ActivityCompat.requestPermissions(activity, arrayOfNecessaryPermission, 1);
        }else{
            checkPermissions.OnPermissionApplied();
        }
    }

    private void checkToughPermissions(){
        checkDrawOverlayPermission();
        checkNotificationPolicyAccess();
    }

    public void checkPermissionsResultByRequestCode(int requestCode){
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE){
            new Handler().postDelayed(checkOverlay, 3000);
        }

        if (requestCode == ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS_REQUEST_CODE){
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (!notificationManager.isNotificationPolicyAccessGranted()) {
                checkNotificationPolicyAccess();
            }
        }
    }

    private Runnable checkOverlay = new Runnable() {
        @Override
        public void run() {
            if (!Settings.canDrawOverlays(activity)) {
                checkDrawOverlayPermission();
            }
        }
    };

    public void checkPermissionsOnResult (@NonNull int[] grantResults){
        if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            checkLightPermissions();
        }else{
            checkPermissions.OnPermissionApplied();
        }
    }

    private void checkDrawOverlayPermission() {
        /** check if we already  have permission to draw over other apps */
        //Only for Api 23 or Higher
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (!Settings.canDrawOverlays(context)) {
                /** if not construct intent to request permission */
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + activity.getPackageName()));
                /** request permission via start activity for result */
                activity.startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void checkNotificationPolicyAccess() {
        /** check if we already  have permission to draw over other apps */
        //Only for Api 23 or Higher
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (!notificationManager.isNotificationPolicyAccessGranted()) {

                Intent intent = new Intent(
                        Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

                activity.startActivityForResult(intent,ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS_REQUEST_CODE);
            }
        }
    }
}
