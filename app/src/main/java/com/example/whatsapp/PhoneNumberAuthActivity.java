package com.example.whatsapp;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.whatsapp.databinding.ActivityPhoneNumberAuthBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PhoneNumberAuthActivity extends AppCompatActivity {
    ActivityPhoneNumberAuthBinding binding;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPhoneNumberAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, READ_SMS) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        String phoneNumber = telephonyManager.getLine1Number();
        binding.phonefield.setText(phoneNumber);

        binding.countryCode.registerCarrierNumberEditText(binding.phonefield);

        binding.next.setOnClickListener(v -> {
            if(binding.phonefield.getText().toString().length()==0){
                binding.phonefield.setError("Please enter number");
            }
            else {
                Intent intent=new Intent(getApplicationContext(), OTPVerify.class);
                intent.putExtra("mobile",binding.countryCode.getFullNumberWithPlus().replace(" ",""));
                startActivity(intent);
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menuauth,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.gmailsignup:
                Intent in=new Intent(PhoneNumberAuthActivity.this,SignUpActivity.class);
                startActivity(in);
                break;
            case R.id.gmailsignin:
                Intent intent=new Intent(PhoneNumberAuthActivity.this,SignInActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}