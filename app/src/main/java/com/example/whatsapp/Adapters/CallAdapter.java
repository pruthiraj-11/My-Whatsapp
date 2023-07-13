package com.example.whatsapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.Listeners.UsersListener;
import com.example.whatsapp.Models.Users;
import com.example.whatsapp.R;

import java.util.ArrayList;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.ViewHolder> {
    private ArrayList<Users> list=new ArrayList<>();
    private final UsersListener usersListener;
    public CallAdapter(ArrayList<Users> list, UsersListener usersListener) {

        this.list = list;
        this.usersListener=usersListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users=list.get(position);
        holder.userNameVideo.setText(users.getUsername());
        holder.dp.setText(users.getUsername().substring(0,1));
        holder.usermailVideo.setText(users.getMail());
        holder.audioImg.setOnClickListener(v -> usersListener.initiateAudioCall(users));
        holder.videoImg.setOnClickListener(v -> usersListener.initiateVideoCall(users));
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView dp, userNameVideo, usermailVideo;
        ImageView audioImg, videoImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dp=itemView.findViewById(R.id.textView18);
            userNameVideo=itemView.findViewById(R.id.userNameStatus);
            usermailVideo=itemView.findViewById(R.id.statusTime);
            audioImg=itemView.findViewById(R.id.callbtn);
            videoImg=itemView.findViewById(R.id.videochat);
        }
    }
}
