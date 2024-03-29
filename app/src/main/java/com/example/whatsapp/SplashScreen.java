package com.example.whatsapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Objects;

public class SplashScreen extends AppCompatActivity {
//    boolean flag1=false;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();
//        int PERMISSION_ALL = 1;
//        String[] PERMISSIONS = {
//                Manifest.permission.CAMERA,
//                Manifest.permission.READ_CONTACTS,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.READ_PHONE_NUMBERS
//        };
//        if (!hasPermissions(this, PERMISSIONS)) {
//            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
//        }
        requestPermissions();
//        SharedPreferences sharedPreferences=getSharedPreferences("save", Context.MODE_PRIVATE);
//        boolean flag=sharedPreferences.getBoolean("value",false);
//        new Handler().postDelayed(() -> {
//            if(flag){
//                startActivity(new Intent(SplashScreen.this,BiometricAuthenticationActivity.class));
//                finish();
//            }
//            else {
//                startActivity(new Intent(SplashScreen.this, SignUpActivity.class));
//                finish();
//            }
//        }, 2000);
        
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    private void requestPermissions() {
        Dexter.withActivity(SplashScreen.this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_PHONE_NUMBERS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            sharedPreferences=getSharedPreferences("save", Context.MODE_PRIVATE);
                            boolean flag=sharedPreferences.getBoolean("value",false);
                            if(flag){
                                startActivity(new Intent(SplashScreen.this,BiometricAuthenticationActivity.class));
                                finish();
                            }
                            else {
                                startActivity(new Intent(SplashScreen.this, SignUpActivity.class));
                                finish();
                            }
                           Toast.makeText(getApplicationContext(), "All the permissions are granted", Toast.LENGTH_SHORT).show();
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(error -> {
                    Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                })
                .onSameThread().check();
    }
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("Goto Settings", (dialog, which) -> {
            dialog.cancel();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}