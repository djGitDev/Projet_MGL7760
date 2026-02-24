package com.example.projet3.model;

public enum TypeTache {
    BASIC(10),
    /** le type Basic a 10 ponits pour calculer le score du membre */
    MEDIUM(20),
    /** le type Medium a 20 ponits calculer le score du membre */
    PROFESSIONNEL(50);

    /** le type Professionnel a 50 ponits calculer le score du membre */

    private final int points;

    /** Constructeur pour associer à chaque type des points */
    TypeTache(int points) {
        this.points = points;
    }

    /** Getter pour récupérer les points de chaque type de tache */
    public int getPoints() {
        return points;
    }
}
