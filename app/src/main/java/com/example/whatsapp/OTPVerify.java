package com.example.whatsapp;

import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsapp.Models.Users;
import com.example.whatsapp.databinding.ActivityOtpverifyBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OTPVerify extends AppCompatActivity {

    ActivityOtpverifyBinding binding;
    FirebaseDatabase database;
    String phonenumber;
    String otpid;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityOtpverifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        phonenumber= getIntent().getStringExtra("mobile");
        String name="Waiting to automatically detect an SMS sent to ";
        String sourceString =name+"<b>"+phonenumber+"</b>";
        binding.textView17.setText(Html.fromHtml(sourceString));
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        initiateotp();
        binding.verifybtn.setOnClickListener(v -> {
            if (binding.otpfield.getText().toString().isEmpty()){
                binding.otpfield.setError("Please enter OTP");
            } else if (binding.otpfield.getText().toString().length()!=6) {
                binding.otpfield.setError("Invalid OTP");
            } else {
                PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otpid,binding.otpfield.getText().toString());
                signInWithPhoneAuthCredential(credential);
            }
        });
    }
    private void initiateotp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonenumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken){
                        otpid=s;
                    }
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Users users=new Users(phonenumber);
                        FirebaseUser user = task.getResult().getUser();
                        String id= Objects.requireNonNull(user).getUid();
                        database.getReference().child("Users").child(id).setValue(users);
                        Toast.makeText(getApplicationContext(), "User created successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Invalid code",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}