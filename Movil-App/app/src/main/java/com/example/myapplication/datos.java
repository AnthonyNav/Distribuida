package com.example.myapplication;

public class datos {
    private int id;
    private String nom;
    private String app;
    private String tel;
    private String email;
    private String clave;

    public datos(int id, String nom, String app, String tel, String email, String clave) {
        this.id = id;
        this.nom = nom;
        this.app = app;
        this.tel = tel;
        this.email = email;
        this.clave = clave;
    }
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getApp() { return app; }
    public String getTel() { return tel; }
    public String getEmail() { return email; }
    public String getClave() { return clave; }
}
