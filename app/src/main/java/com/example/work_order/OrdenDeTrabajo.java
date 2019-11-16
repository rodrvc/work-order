package com.example.work_order;

public class OrdenDeTrabajo {


    private String uid, title, description;

    public OrdenDeTrabajo(String uid, String title, String description) {
        this.uid = uid;
        this.title = title;
        this.description = description;
    }



    public OrdenDeTrabajo() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
}
