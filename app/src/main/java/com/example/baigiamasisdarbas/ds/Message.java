package com.example.baigiamasisdarbas.ds;

public class Message {

    private String senderId;
    private String senderFullName;
    private String message;

    private String apartmentBuilding;

    private String date;


    public Message(String senderFullName, String message, String apartmentBuilding) {
        this.senderFullName = senderFullName;
        this.message = message;
        this.apartmentBuilding = apartmentBuilding;
        this.date = date;
    }

    public Message(String senderId, String senderFullName, String message, String apartmentBuilding, String date) {
        this.senderId = senderId;
        this.senderFullName = senderFullName;
        this.message = message;
        this.apartmentBuilding = apartmentBuilding;
        this.date = date;
    }

    public Message() {
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getApartmentBuilding() {
        return apartmentBuilding;
    }

    public void setApartmentBuilding(String apartmentBuilding) {
        this.apartmentBuilding = apartmentBuilding;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "senderId='" + senderFullName + '\'' +
                ", message='" + message + '\'' +
                ", apartmentBuilding='" + apartmentBuilding + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
