package com.example.whatsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsapp.Adapters.FragmentsAdapter;
import com.example.whatsapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        database=FirebaseDatabase.getInstance();

        sharedPreferences=getSharedPreferences("dndmodeflag", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("dndisenabled",false);
        editor.apply();

        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tablayout.setupWithViewPager(Objects.requireNonNull(binding.viewPager));

    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menus,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.settings:
                Intent in=new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(in);
                break;
            case R.id.logout:
                auth.signOut();
                Intent intent=new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.grouchat:
                Intent i=new Intent(MainActivity.this,GroupChatActivity.class);
                startActivity(i);
                break;
            case R.id.dnd:
                sharedPreferences = getSharedPreferences("dndmodeflag",Context.MODE_PRIVATE);
                boolean flag;
                flag=sharedPreferences.getBoolean("dndisenabled",false);
                if(!flag){
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean("dndisenabled",true);
                    editor.apply();
                    database.goOffline();
                }
                else{
                    database.goOnline();
                }
                break;
        }
        return true;
    }
}