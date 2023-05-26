package com.example.baigiamasisdarbas.ds;

public class Survey {

    private String id;
    private String title;
    private String description;
    private String creationDate;
    private String modificationDate;

    private String apartment;
    private String url;

    public Survey() {
    }

    public Survey(String title, String description, String creationDate, String modificationDate, String url, String apartment) {
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.url = url;
        this.apartment = apartment;
    }

    public Survey(String id, String title, String description, String url, String apartment) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.apartment = apartment;
    }


    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
