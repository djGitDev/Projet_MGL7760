package com.example.projet3.model;

import java.util.Arrays;
import java.util.Optional;

public enum Sequence {
    PREPARATION_SITE("Préparation du Site"),
    FONDATIONS("Fondations"),
    CONSTRUCTION_STRUCTURE("Construction de la Structure"),
    CLOISONS_FENETRES("Cloisons, Fenêtres et Toiture"),
    INSTALLATION_SYSTEMES("Installation des Systèmes Techniques"),
    FINITIONS_SECURISATION("Finitions et Sécurisation"),
    NETTOYAGE_RECEPTION("Nettoyage et Réception");

    private String nom;

    Sequence(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    // Trouver une séquence par son nom
    public static Optional<Sequence> fromLibelle(String libelle) {
        return Arrays.stream(values())
                .filter(seq -> seq.getNom().equalsIgnoreCase(libelle))
                .findFirst();
    }

}
