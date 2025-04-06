package com.example.projetws.beans;

public class Etudiant {

    private int id;
    private String nom;
    private String prenom;
    private String ville;
    private String sexe;
    private String date_naissance;
    private String image;


    public Etudiant(int id, String nom, String prenom, String ville, String sexe, String date_naissance, String image) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.ville = ville;
        this.sexe = sexe;
        this.date_naissance = date_naissance;
        this.image = image;
    }


    public int getId() {
        return id;
    }


    public String getNom() {
        return nom;
    }


    public String getPrenom() {
        return prenom;
    }


    public String getVille() {
        return ville;
    }


    public String getSexe() {
        return sexe;
    }

    public String getDateNaissance() {
        return date_naissance;
    }

    public String getImage() {
        return image;
    }
}
