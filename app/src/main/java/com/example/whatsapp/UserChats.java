package com.example.whatsapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.whatsapp.Adapters.ChatAdapter;
import com.example.whatsapp.Models.MessageModel;
import com.example.whatsapp.databinding.ActivityUserChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class UserChats extends AppCompatActivity {
    ActivityUserChatsBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityUserChatsBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();
        database = FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        final String senderId = auth.getUid();
        String receiveId = getIntent().getStringExtra("userId");
        String userName=getIntent().getStringExtra("username");
        String profilPic=getIntent().getStringExtra("profilePic");
        binding.userName.setText(userName);
        Picasso.get().load(profilPic).placeholder(R.mipmap.ic_launcher_round).into(binding.profilePhoto);
        binding.backarrow.setOnClickListener(v -> {
            Intent intent=new Intent(UserChats.this,MainActivity.class);
            startActivity(intent);
        });
        final ArrayList<MessageModel> messagesModels=new ArrayList<>();
        final ChatAdapter chatAdapter= new ChatAdapter(messagesModels,this,receiveId);
        binding.chatrecyclerview.setAdapter(chatAdapter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        binding.chatrecyclerview.setLayoutManager(linearLayoutManager);

        final String senderRoom=senderId+receiveId;
        final String receiverRoom=receiveId+senderId;

        database.getReference().child("Chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesModels.clear();
                for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                    MessageModel model=dataSnapshot1.getValue(MessageModel.class);
                    Objects.requireNonNull(model).setMessageId(dataSnapshot1.getKey());
                    messagesModels.add(model);
                }
                chatAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        binding.sendbtn.setOnClickListener(v -> {
            String message= binding.msgEnter.getText().toString();
            final MessageModel model=new MessageModel(senderId,message);
            model.setTimeStamp(new Date().getTime());
            binding.msgEnter.setText("");
            database.getReference().child("Chats").child(senderRoom).push().setValue(model).addOnSuccessListener(unused -> database.getReference().child("Chats").child(receiverRoom).push().setValue(model).addOnSuccessListener(unused1 -> {}));
        });
    }
}