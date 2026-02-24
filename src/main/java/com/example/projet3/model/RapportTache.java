package com.example.projet3.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class RapportTache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public String toString() {
        return "RapportTache [id=" + id + ", dateRapport=" + dateRapport + ", commentaire=" + commentaire + ", etat="
                + etat + ", tache=" + tache + ", membre=" + membre + "]";
    }

    private LocalDate dateRapport;

    @Column(length = 1000)
    private String commentaire;

    private String etat;

    @ManyToOne
    @JoinColumn(name = "tache_id")
    private Tache tache;

    @ManyToOne
    @JoinColumn(name = "membre_id")
    private Membre membre;

    public RapportTache() {
    }

    public RapportTache(LocalDate dateRapport, String commentaire, String etat, Tache tache, Membre membre) {
        this.dateRapport = dateRapport;
        this.commentaire = commentaire;
        this.etat = etat;
        this.tache = tache;
        this.membre = membre;
    }
    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDateRapport(LocalDate dateRapport) {
        this.dateRapport = dateRapport;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setTache(Tache tache) {
        this.tache = tache;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    public LocalDate getDateRapport() {
        return dateRapport;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public String getEtat() {
        return etat;
    }

    public Tache getTache() {
        return tache;
    }

    public Membre getMembre() {
        return membre;
    }

}
