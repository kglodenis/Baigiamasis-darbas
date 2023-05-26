package com.example.baigiamasisdarbas.ds;

import java.util.ArrayList;

public class ApartmentBuilding {

    private String address;
    private int floors;
    private String responsiblePersonNameAndSurname;
    private String responsiblePersonNumber;
    private String registrationCode;
    private ArrayList<String> allResidents;
    private ArrayList<String> allRequests;
    private ArrayList<String> cleaningSchedule;


    public ApartmentBuilding(String address, int floors, String responsiblePersonNameAndSurname, String responsiblePersonNumber, String registrationCode) {
        this.address = address;
        this.floors = floors;
        this.responsiblePersonNameAndSurname = responsiblePersonNameAndSurname;
        this.responsiblePersonNumber = responsiblePersonNumber;
        this.registrationCode = registrationCode;
        this.cleaningSchedule = new ArrayList<>();

    }

    public ApartmentBuilding(String address, int floors, String responsiblePersonNameAndSurname, String responsiblePersonNumber, String registrationCode, ArrayList<String> cleaningSchedule) {
        this.address = address;
        this.floors = floors;
        this.responsiblePersonNameAndSurname = responsiblePersonNameAndSurname;
        this.responsiblePersonNumber = responsiblePersonNumber;
        this.registrationCode = registrationCode;
        this.cleaningSchedule = cleaningSchedule;

    }


    public ApartmentBuilding() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public String getResponsiblePersonNameAndSurname() {
        return responsiblePersonNameAndSurname;
    }

    public void setResponsiblePersonNameAndSurname(String responsiblePersonNameAndSurname) {
        this.responsiblePersonNameAndSurname = responsiblePersonNameAndSurname;
    }

    public String getResponsiblePersonNumber() {
        return responsiblePersonNumber;
    }

    public void setResponsiblePersonNumber(String responsiblePersonNumber) {
        this.responsiblePersonNumber = responsiblePersonNumber;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public ArrayList<String> getAllResidents() {
        return allResidents;
    }


    public ArrayList<String> getAllRequests() {
        return allRequests;
    }

    public void setAllRequests(ArrayList<String> allRequests) {
        this.allRequests = allRequests;
    }

    public ArrayList<String> getCleaningSchedule() {
        return cleaningSchedule;
    }

    public void setCleaningSchedule(ArrayList<String> cleaningSchedule) {
        this.cleaningSchedule = cleaningSchedule;
    }


    @Override
    public String toString() {
        return "ApartmentBuilding{" +
                "address='" + address + '\'' +
                ", floors=" + floors +
                ", responsiblePersonNameAndSurname='" + responsiblePersonNameAndSurname + '\'' +
                ", responsiblePersonNumber='" + responsiblePersonNumber + '\'' +
                ", allResidents=" + allResidents +
                '}';
    }
}
