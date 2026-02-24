package com.example.projet3.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "organisations")

public class Organisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String type;

    // des Champs a calculés (non persistés en base de données)
    @Transient
    private long nbMembres;

    @Transient
    private int scoreTotal;

    @Transient
    private long nbTachesEffectuees;
    // Relation OneToMany avec Membre
    @OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Membre> membres;

    @OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tache> taches;

    @OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Outil> outils;

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getType() {
        return type;
    }

    public Organisation(String nom, String type) {
        this.type = type;
        this.nom = nom;
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

    @Override
    public String toString() {
        return "Organisation [id=" + id + ", nom=" + nom + ", type=" + type + "]";
    }

    public Organisation() {
    }

    public List<Membre> getMembres() {
        return membres;
    }

    public void setTaches(List<Tache> taches) {
        this.taches = taches;
    }

    public void setOutils(List<Outil> outils) {
        this.outils = outils;
    }

    public List<Tache> getTaches() {
        return taches;
    }

    public List<Outil> getOutils() {
        return outils;
    }

    public void setMembres(List<Membre> membres) {
        this.membres = membres;
    }

    public long getNbMembres() {
        return nbMembres;
    }

    public void setNbMembres(long nbMembres) {
        this.nbMembres = nbMembres;
    }

    public int getScoreTotal() {
        return scoreTotal;
    }

    public void setScoreTotal(int scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    public long getNbTachesEffectuees() {
        return nbTachesEffectuees;
    }

    public void setNbTachesEffectuees(long nbTachesEffectuees) {
        this.nbTachesEffectuees = nbTachesEffectuees;
    }

}
