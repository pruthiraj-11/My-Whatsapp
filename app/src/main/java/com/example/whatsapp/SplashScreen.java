package com.example.whatsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        Objects.requireNonNull(getSupportActionBar()).hide();

        SharedPreferences sharedPreferences=getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        boolean flag=sharedPreferences.getBoolean("switchstate",false);

        new Handler().postDelayed(() -> {
            if(flag){
                startActivity(new Intent(SplashScreen.this,BiometricAuthenticationActivity.class));
                finish();
            }
            else {
                startActivity(new Intent(SplashScreen.this,PhoneNumberAuthActivity.class));
                finish();
            }
        }, 5000);

    }
}