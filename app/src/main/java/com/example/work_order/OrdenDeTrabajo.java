package com.example.work_order;

public class OrdenDeTrabajo {


    private String uid, title, description , idUsuario;


    public OrdenDeTrabajo() {
    }

    public OrdenDeTrabajo(String uid, String title, String description, String idUsuario) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.idUsuario = idUsuario;
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

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
