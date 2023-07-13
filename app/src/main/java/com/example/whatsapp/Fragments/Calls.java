package com.example.whatsapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.whatsapp.Adapters.CallAdapter;
import com.example.whatsapp.Listeners.UsersListener;
import com.example.whatsapp.Models.Users;
import com.example.whatsapp.databinding.FragmentCallsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Calls extends Fragment implements UsersListener {
    FragmentCallsBinding binding;
    ArrayList<Users> list=new ArrayList<Users>();
    FirebaseDatabase database;
    public Calls() {}
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        binding=FragmentCallsBinding.inflate(inflater, container, false);
        CallAdapter callAdapter=new CallAdapter(list,this);
        binding.callRecyclerView.setAdapter(callAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        binding.callRecyclerView.setLayoutManager(linearLayoutManager);
        database= FirebaseDatabase.getInstance();
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Users users=dataSnapshot.getValue(Users.class);
                    Objects.requireNonNull(users).setUserId(dataSnapshot.getKey());
                    list.add(users);
                }
                callAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        return binding.getRoot();
    }
    @Override
    public void initiateVideoCall(Users users) {

    }

    @Override
    public void initiateAudioCall(Users users) {

    }
}