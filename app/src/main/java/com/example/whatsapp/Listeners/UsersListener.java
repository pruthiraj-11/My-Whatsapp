package com.example.whatsapp.Listeners;

import com.example.whatsapp.Models.Users;

public interface UsersListener {

    void initiateVideoCall(Users users);
    void initiateAudioCall(Users users);
}
