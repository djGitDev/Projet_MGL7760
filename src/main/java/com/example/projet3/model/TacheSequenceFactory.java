package com.example.projet3.model;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class TacheSequenceFactory {
    private static final List<String> SEQ_NOMS = List.of(
            "Préparation du Site", "Fondations", "Construction de la Structure",
            "Cloisons, Fenêtres et Toiture", "Installation des Systèmes Techniques",
            "Finitions et Sécurisation", "Nettoyage et Réception");

    public List<Tache> createSequence(String type) {
        List<Tache> seq = new ArrayList<>();
        boolean pro = "PROFESSIONNEL".equalsIgnoreCase(type);
        for (int i = 0; i < SEQ_NOMS.size(); i++) {
            String nom = SEQ_NOMS.get(i);
            int sc = switch (nom) {
                case "Préparation du Site" -> pro ? 18 : 5;
                case "Fondations" -> pro ? 30 : 9;
                case "Construction de la Structure" -> pro ? 40 : 10;
                case "Cloisons, Fenêtres et Toiture" -> pro ? 25 : 7;
                case "Installation des Systèmes Techniques" -> pro ? 30 : 8;
                case "Finitions et Sécurisation" -> pro ? 22 : 6;
                case "Nettoyage et Réception" -> pro ? 15 : 4;
                default -> 0;
            };
            /*
             * Tache t = new Tache(nom, sc, i);
             * t.setEtat(i == 0 ? EtatTache.PLANNED : EtatTache.PENDING);
             * seq.add(t);
             */
        }
        return seq;
    }
}
