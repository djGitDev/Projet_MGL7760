package com.example.projet3.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvaluationTacheTest {

    @Test
    void testConstructeurVideEtSettersGetters() {
        EvaluationTache evaluation = new EvaluationTache();

        Tache tache = new Tache();
        Membre membre = new Membre();

        evaluation.setId(1L);
        evaluation.setScore(10);
        evaluation.setCommentaire("Bon travail");
        evaluation.setTache(tache);
        evaluation.setMembre(membre);

        assertEquals(1L, evaluation.getId());
        assertEquals(10, evaluation.getScore());
        assertEquals("Bon travail", evaluation.getCommentaire());
        assertEquals(tache, evaluation.getTache());
        assertEquals(membre, evaluation.getMembre());
    }
}