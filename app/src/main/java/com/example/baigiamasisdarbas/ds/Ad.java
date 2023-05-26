package com.example.baigiamasisdarbas.ds;

public class Ad {

    private String id;
    private String companyName;
    private String title;
    private String description;
    private String date;
    private String modificationDate;
    private String apartment;

    public Ad() {
    }

    public Ad(String companyName, String title, String description, String date, String apartment) {

        this.companyName = companyName;
        this.title = title;
        this.description = description;
        this.date = date;
        this.apartment = apartment;
    }

    public Ad(String id, String companyName, String title, String description, String date, String apartment) {
        this.id = id;
        this.companyName = companyName;
        this.title = title;
        this.description = description;
        this.date = date;
        this.apartment = apartment;
    }

    public Ad(String id, String companyName, String title, String description, String date, String apartment, String modificationDate) {
        this.id = id;
        this.companyName = companyName;
        this.title = title;
        this.description = description;
        this.date = date;
        this.apartment = apartment;
        this.modificationDate = modificationDate;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }
}
