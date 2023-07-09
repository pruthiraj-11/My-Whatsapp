package com.example.whatsapp;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Objects;

public class BiometricAuthenticationActivity extends AppCompatActivity {

    private CancellationSignal cancellationSignal = null;
    private BiometricPrompt.AuthenticationCallback authenticationCallback;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric_authentication);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Objects.requireNonNull(getSupportActionBar()).hide();

            authenticationCallback = new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    notifyUser("Fingerprint not recognized");
                }
                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    startActivity(new Intent(BiometricAuthenticationActivity.this,PhoneNumberAuthActivity.class));
                }
            };
            checkBiometricSupport();
                    BiometricPrompt biometricPrompt = new BiometricPrompt
                            .Builder(getApplicationContext())
                            .setTitle("Verify it's you")
                            .setSubtitle("Unlock to enter into Whatsapp")
                            .setDescription("Touch the fingerprint sensor")
                            .setNegativeButton("Cancel", getMainExecutor(), (dialogInterface, i) -> {
                                notifyUser("Authentication Cancelled");
//                                    finish();
                            }).build();
                    biometricPrompt.authenticate(
                            getCancellationSignal(),
                            getMainExecutor(),
                            authenticationCallback);
        }
        private CancellationSignal getCancellationSignal(){
            cancellationSignal = new CancellationSignal();
            cancellationSignal.setOnCancelListener(
                    () -> notifyUser("Authentication was Cancelled by the user"));
            return cancellationSignal;
        }
        @RequiresApi(Build.VERSION_CODES.M)
        private Boolean checkBiometricSupport(){
            KeyguardManager keyguardManager = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
            if (!keyguardManager.isDeviceSecure()) {
                notifyUser("Fingerprint authentication has not been enabled in settings");
                return false;
            }
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC)!= PackageManager.PERMISSION_GRANTED) {
                notifyUser("Fingerprint Authentication Permission is not enabled");
                return false;
            }
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
                return true;
            }
            else{
                return true;
            }
        }
        private void notifyUser(String message) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
}