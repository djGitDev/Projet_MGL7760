package com.example.projet3.model;

public class TacheDTO {
    private String nom;
    private int dureeEstimee;
    private String description;

    // Optionnel : type de tâche si nécessaire
    private String type;

    // getters et setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public int getDureeEstimee() { return dureeEstimee; }
    public void setDureeEstimee(int dureeEstimee) { this.dureeEstimee = dureeEstimee; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
