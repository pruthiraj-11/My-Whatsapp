package com.example.whatsapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.whatsapp.Adapters.ChatAdapter;
import com.example.whatsapp.Models.MessageModel;
import com.example.whatsapp.databinding.ActivityGroupChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        binding.backarrow.setOnClickListener(v -> {
            Intent intent=new Intent(GroupChatActivity.this,MainActivity.class);
            startActivity(intent);
        });

        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final ArrayList<MessageModel> messageModels=new ArrayList<>();

        final String senderId= FirebaseAuth.getInstance().getUid();
        binding.userName.setText("Friends Group");

        final ChatAdapter adapter=new ChatAdapter(messageModels,this);
        binding.chatrecyclerview.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        binding.chatrecyclerview.setLayoutManager(linearLayoutManager);

        database.getReference().child("Group Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();
                for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                    MessageModel model=dataSnapshot1.getValue(MessageModel.class);
                    messageModels.add(model);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        binding.sendbtn.setOnClickListener(v -> {
            String message= binding.msgEnter.getText().toString();
            final MessageModel model=new MessageModel(senderId,message);
            Calendar calendar=Calendar.getInstance();
            Date date=calendar.getTime();
//            model.setTimeStamp(new Date().getTime());
            model.setTimeStamp(date.toString());
            binding.msgEnter.setText("");
            database.getReference().child("Group Chat").push().setValue(model).addOnSuccessListener(unused -> database.getReference().child("Chats").push().setValue(model).addOnSuccessListener(unused1 -> {
            }));

        });


    }
}