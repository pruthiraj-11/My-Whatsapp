package com.example.whatsapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.Models.MessageModel;
import com.example.whatsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {
    ArrayList<MessageModel> messagesModels=new ArrayList<MessageModel>();
    Context context;
    String recId;
    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;

    public ChatAdapter(ArrayList<MessageModel> messagesModels, Context context) {
        this.messagesModels = messagesModels;
        this.context = context;
    }
    public ChatAdapter(ArrayList<MessageModel> messagesModels, Context context, String recId) {
        this.messagesModels = messagesModels;
        this.context = context;
        this.recId = recId;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==SENDER_VIEW_TYPE){
            view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewHolder(view);
        }
        else{
            view = LayoutInflater.from(context).inflate(R.layout.sample_receiver, parent, false);
            return new ReceiverViewHolder(view);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(messagesModels.get(position).getuId()
                .equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }
        else{
            return RECEIVER_VIEW_TYPE;
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messagesModel=messagesModels.get(position);
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete")
                    .setMessage("Are you sure want to delete?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        String sender=FirebaseAuth.getInstance().getUid()+recId;
                        database.getReference().child("Chats").child(sender)
                                .child(messagesModel.getMessageId())
                                .setValue(null);
                    }).setNegativeButton("No", (dialog, which) -> dialog.dismiss()).show();
            return false;
        });

        if(holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder) holder).senderMsg.setText(messagesModel.getMessage());
//            ((SenderViewHolder) holder).senderTime.setText(messagesModel.getTimeStamp());
        }
        else{
            ((ReceiverViewHolder) holder).receiverMsg.setText(messagesModel.getMessage());
//            ((ReceiverViewHolder) holder).receiverTime.setText(messagesModel.getTimeStamp());
        }
    }
    @Override
    public int getItemCount() {
        return messagesModels.size();
    }

    public static class ReceiverViewHolder extends RecyclerView.ViewHolder{
        TextView receiverMsg, receiverTime;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMsg = itemView.findViewById(R.id.receiverText);
            receiverTime=itemView.findViewById(R.id.receiverTime);
        }
    }
    public static class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senderMsg, senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg= itemView.findViewById(R.id.senderText);
            senderTime=itemView.findViewById(R.id.senderTime);
        }
    }
}
