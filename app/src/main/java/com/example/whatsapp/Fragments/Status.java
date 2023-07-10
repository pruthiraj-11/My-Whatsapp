package com.example.whatsapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsapp.R;
import com.example.whatsapp.databinding.FragmentChatsBinding;
import com.example.whatsapp.databinding.FragmentStatusBinding;

public class Status extends Fragment {

    FragmentStatusBinding binding;

    public Status() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentStatusBinding.inflate(inflater,container,false);


        return binding.getRoot();
    }
}