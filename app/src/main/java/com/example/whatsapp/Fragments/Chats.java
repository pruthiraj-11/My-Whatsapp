package com.example.whatsapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.whatsapp.Adapters.UserAdapter;
import com.example.whatsapp.ArchiveChatsActivity;
import com.example.whatsapp.Models.Users;
import com.example.whatsapp.databinding.FragmentChatsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Chats extends Fragment {
    public Chats() {}
    FragmentChatsBinding binding;
    ArrayList<Users> list=new ArrayList<Users>();
    FirebaseDatabase database;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentChatsBinding.inflate(inflater,container,false);
        registerForContextMenu(binding.chatrecyclerview);
        UserAdapter userAdapter=new UserAdapter(list,getContext());
        binding.chatrecyclerview.setAdapter(userAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        binding.chatrecyclerview.setLayoutManager(linearLayoutManager);
        database=FirebaseDatabase.getInstance();
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Users users=dataSnapshot.getValue(Users.class);
                    Objects.requireNonNull(users).setUserId(dataSnapshot.getKey());
                    list.add(users);
                }
                userAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        return binding.getRoot();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);
        switch (item.getItemId()){
            case 101:
                Toast.makeText(getContext(),"Archived",Toast.LENGTH_SHORT).show();
                return true;
            case 102:
                Toast.makeText(getContext(),"Deleted",Toast.LENGTH_SHORT).show();
                return true;
        }
        return true;
    }
}