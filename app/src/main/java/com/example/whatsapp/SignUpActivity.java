package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.whatsapp.Models.Users;
import com.example.whatsapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog dialog;
    GoogleSignInClient googleSignInClient;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        dialog=new ProgressDialog(SignUpActivity.this);
        btn=findViewById(R.id.gup);
        dialog.setTitle("Creating Account");
        dialog.setMessage("We are creating our account");
        binding.sup.setOnClickListener(v -> {
            dialog.show();
            String email = String.valueOf(binding.mailfield.getText());
            String pass = String.valueOf(binding.passfield.getText());
            auth.createUserWithEmailAndPassword(email, pass).
                    addOnCompleteListener(task -> {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            Users user=new Users(binding.uname.getText().toString(),email,pass);
                            String id= Objects.requireNonNull(task.getResult().getUser()).getUid();
                            database.getReference().child("Users").child(id).setValue(user);
                            Toast.makeText(SignUpActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        binding.ap.setOnClickListener(v -> {
            Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
            startActivity(intent);
        });
    }
}