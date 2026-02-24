package com.example.projet3.model;

import jakarta.persistence.*;

@Entity
public class EvaluationTache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score;

    private String commentaire;

    @ManyToOne
    @JoinColumn(name = "tache_id")
    private Tache tache;

    @ManyToOne
    @JoinColumn(name = "membre_id")
    private Membre membre;

    @Override
    public String toString() {
        return "EvaluationTache [id=" + id + ", score=" + score + ", commentaire=" + commentaire + ", tache=" + tache
                + ", membre=" + membre + "]";
    }

    public EvaluationTache() {
    }

    public EvaluationTache(int score, String commentaire, Tache tache, Membre membre) {
        this.score = score;
        this.commentaire = commentaire;
        this.tache = tache;
        this.membre = membre;
    }
    // Getters & Setters

    public Long getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public void setTache(Tache tache) {
        this.tache = tache;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public Tache getTache() {
        return tache;
    }

    public Membre getMembre() {
        return membre;
    }

}
