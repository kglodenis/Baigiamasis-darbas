package com.example.baigiamasisdarbas.ds;

public class Request {
    private String requestId;
    private String requestDescription;
    private String senderID;
    private String senderFullName;
    private String phoneNumber;
    private String registrationDate;
    private String image;
    private String requestStatus;

    private String apartmentBuilding;

    private String requestType;

    private String modificationDate;

    public Request() {
    }

    public Request(String requestDescription, String senderID, String phoneNumber, String registrationDate, String modificationDate, RequestStatus requestStatus, String requestType) {
        this.requestDescription = requestDescription;
        this.senderID = senderID;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
        this.modificationDate = modificationDate;
        this.requestStatus = requestStatus.name();
        this.requestType = requestType;

    }


    public Request(String requestId, String senderFullName, String apartmentBuilding, String requestDescription, String requestStatus, String registrationDate, String image, String phoneNumber) {
        this.requestId = requestId;
        this.senderFullName = senderFullName;
        this.apartmentBuilding = apartmentBuilding;
        this.requestDescription = requestDescription;
        this.requestStatus = requestStatus;
        this.registrationDate = registrationDate;
        this.image = image;
        this.phoneNumber = phoneNumber;
    }

    public Request(String requestId, String senderID, String requestDescription, String requestStatus, String registrationDate, String apartmentBuilding, String phoneNumber, String senderFullName, String modificationDate) {

        this.requestId = requestId;
        this.senderID = senderID;
        this.requestDescription = requestDescription;
        this.requestStatus = requestStatus;
        this.registrationDate = registrationDate;
        this.apartmentBuilding = apartmentBuilding;
        this.phoneNumber = phoneNumber;
        this.senderFullName = senderFullName;
        this.modificationDate = modificationDate;
    }

    public Request(String senderID, String requestDescription) {
        this.senderID = senderID;
        this.requestDescription = requestDescription;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestDescription() {
        return requestDescription;
    }

    public void setRequestDescription(String requestDescription) {
        this.requestDescription = requestDescription;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getApartmentBuilding() {
        return apartmentBuilding;
    }

    public void setApartmentBuilding(String apartmentBuilding) {
        this.apartmentBuilding = apartmentBuilding;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }


}
