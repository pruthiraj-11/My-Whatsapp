package com.example.whatsapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.Models.StatusModel;
import com.example.whatsapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {
    ArrayList<StatusModel> statusModels;
    Context context;

    public StatusAdapter(ArrayList<StatusModel> statusModels, Context context) {
        this.statusModels=statusModels;
        this.context = context;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.sample_status,parent,false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        StatusModel statusModel=statusModels.get(position);
        holder.username.setText(statusModel.getUsername());
        Picasso.get().load(statusModel.getStatusthumbnail()).placeholder(R.mipmap.ic_launcher_round).into(holder.imageView);

    }


    @Override
    public int getItemCount() {
        return 0;
    }


    public static class StatusViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView username,time;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.statusthumbnail);
            username=itemView.findViewById(R.id.statususername);
            time=itemView.findViewById(R.id.statustimestamp);
        }
    }

}
