package com.example.work_order;

public class User {
    private String nombre;
    private String email;
    private String UserId ;

    public User() {
    }

    public User(String nombre, String email, String userId) {
        this.nombre = nombre;
        this.email = email;
        UserId = userId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
