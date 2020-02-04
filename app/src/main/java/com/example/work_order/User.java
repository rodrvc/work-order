package com.example.work_order;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;

public  class User {
    private String nombre;
    private String email;
    private String UserId ;


    public static FirebaseAuth estadoDeLog = FirebaseAuth.getInstance();

    FirebaseAuth u = FirebaseAuth.getInstance();




    public User() {
    }

    public User(String nombre, String email, String userId) {
        this.nombre = nombre;
        this.email = email;
        this.UserId = userId;
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

    public static FirebaseUser obtenerUsuario(){
        return estadoDeLog.getCurrentUser();
    }

    public static void salirSession(){

         estadoDeLog.signOut();

    }

    public static FirebaseAuth obtenerAutentificacion(){
        return estadoDeLog;
    }

    public void salir(){
        u.signOut();
    }




















}
