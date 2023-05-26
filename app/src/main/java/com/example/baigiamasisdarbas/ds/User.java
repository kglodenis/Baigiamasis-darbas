package com.example.baigiamasisdarbas.ds;

public class User {

    private String userID;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String pictureUri;

    private String dateCreated;

    private String apartmentBuilding;

    private String userType;


    public User(String userID, String name, String surname, String email, String phoneNumber, String apartmentBuilding, String dateCreated) {
        this.userID = userID;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.apartmentBuilding = apartmentBuilding;
        this.dateCreated = dateCreated;
        this.userType = UserType.USER.name();

    }

    public User(String email, String name, String surname, String phoneNumber, String pictureUri) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.pictureUri = pictureUri;
    }

    public User() {
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(String pictureUri) {
        this.pictureUri = pictureUri;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getApartmentBuilding() {
        return apartmentBuilding;
    }

    public void setApartmentBuilding(String apartmentBuilding) {
        this.apartmentBuilding = apartmentBuilding;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
