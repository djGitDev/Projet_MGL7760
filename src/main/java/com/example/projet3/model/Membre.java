
package com.example.projet3.model;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Membre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private LocalDate dateAdhesion;
    // Type de membtre enum (ADMIN, EMPLOYE OU VOLONTAIRE)
    @Enumerated(EnumType.STRING)
    private TypeMembre type;

    // Relation Many‑to‑Many vers Tache
    @ManyToMany(mappedBy = "membres")
    private List<Tache> taches = new ArrayList<>();

    public List<Tache> getTaches() {
        return taches;
    }

    // Relation ManyToOne avec Organisation
    @ManyToOne
    @JoinColumn(name = "organisation_id") // Nom de la colonne dans la table Membre qui référence l'ID de l'organisation
    private Organisation organisation;

    // Relation avec les évaluations de taches
    @OneToMany(mappedBy = "membre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EvaluationTache> evaluations;

    // Constructeurs
    public Membre() {
    }

    @Override
    public String toString() {
        return "Membre [id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", type=" + type + ", dateAdhesion="
                + dateAdhesion + ", organisation=" + organisation + "]";
    }

    public Membre(String nom, String prenom, TypeMembre type, Organisation organisation, LocalDate dateAdhesion) {
        this.nom = nom;
        this.prenom = prenom;
        this.type = type;
        this.organisation = organisation;
        this.dateAdhesion = dateAdhesion;
    }

    // Getters et setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public TypeMembre getType() {
        return type;
    }

    public void setType(TypeMembre type) {
        this.type = type;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public LocalDate getDateAdhesion() {
        return dateAdhesion;
    }

    public void setDateAdhesion(LocalDate dateAdhesion) {
        this.dateAdhesion = dateAdhesion;
    }

    public List<EvaluationTache> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<EvaluationTache> evaluations) {
        this.evaluations = evaluations;
    }

    public void setTaches(List<Tache> taches) {
        this.taches = taches;
    }
}
