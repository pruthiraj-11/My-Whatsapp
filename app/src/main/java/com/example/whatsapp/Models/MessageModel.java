package com.example.whatsapp.Models;

public class MessageModel {
    String uId="",message="",messageId="";
//    Long timeStamp;
    String timeStamp;
    public MessageModel(String uId, String message, String timeStamp) {
        this.uId = uId;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public MessageModel(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }
    public MessageModel(){}

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
