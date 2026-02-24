package com.example.projet3.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Outil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String type;

    private String disponibilite;

    private String dateAchat;

    private int nombreUtilisations;

    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    @ManyToMany(mappedBy = "outils", cascade = CascadeType.ALL)
    private List<Tache> taches = new ArrayList<>();

    public Outil() {
    }

    public Outil(String nom, String type, String disponibilite, String dateAchat, int nombreUtilisations,
            Organisation organisation) {
        this.nom = nom;
        this.type = type;
        this.disponibilite = disponibilite;
        this.dateAchat = dateAchat;
        this.nombreUtilisations = nombreUtilisations;
        this.organisation = organisation;
    }

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDisponibilite(String disponibilite) {
        this.disponibilite = disponibilite;
    }

    public void setDateAchat(String dateAchat) {
        this.dateAchat = dateAchat;
    }

    public void setNombreUtilisations(int nombreUtilisations) {
        this.nombreUtilisations = nombreUtilisations;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    @Override
    public String toString() {
        return "Outil [id=" + id + ", nom=" + nom + ", type=" + type + ", disponibilite=" + disponibilite
                + ", dateAchat=" + dateAchat + ", nombreUtilisations=" + nombreUtilisations + ", organisation="
                + organisation + "]";
    }

    public String getType() {
        return type;
    }

    public String getDisponibilite() {
        return disponibilite;
    }

    public String getDateAchat() {
        return dateAchat;
    }

    public int getNombreUtilisations() {
        return nombreUtilisations;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public List<Tache> getTaches() {
        return taches;
    }

    public void setTaches(List<Tache> taches) {
        this.taches = taches;
    }

}