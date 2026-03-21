package com.example.projet3.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class EvaluationTacheTest {

    @Test
    void defaultConstructorCreatesInstance() {
        EvaluationTache evaluation = new EvaluationTache();
        assertNotNull(evaluation);
    }

    @Test
    void parameterizedConstructorSetsFields() {
        Tache tache = new Tache();
        Membre membre = new Membre();
        EvaluationTache evaluation = new EvaluationTache(85, "Good work", tache, membre);
        assertEquals(85, evaluation.getScore());
        assertEquals("Good work", evaluation.getCommentaire());
        assertEquals(tache, evaluation.getTache());
        assertEquals(membre, evaluation.getMembre());
    }

    @Test
    void setIdAndGetId() {
        EvaluationTache evaluation = new EvaluationTache();
        evaluation.setId(1L);
        assertEquals(1L, evaluation.getId());
    }

    @Test
    void setScoreAndGetScore() {
        EvaluationTache evaluation = new EvaluationTache();
        evaluation.setScore(90);
        assertEquals(90, evaluation.getScore());
    }

    @Test
    void setCommentaireAndGetCommentaire() {
        EvaluationTache evaluation = new EvaluationTache();
        evaluation.setCommentaire("Excellent");
        assertEquals("Excellent", evaluation.getCommentaire());
    }

    @Test
    void setTacheAndGetTache() {
        EvaluationTache evaluation = new EvaluationTache();
        Tache tache = new Tache();
        evaluation.setTache(tache);
        assertEquals(tache, evaluation.getTache());
    }

    @Test
    void setMembreAndGetMembre() {
        EvaluationTache evaluation = new EvaluationTache();
        Membre membre = new Membre();
        evaluation.setMembre(membre);
        assertEquals(membre, evaluation.getMembre());
    }

    @Test
    void toStringReturnsCorrectFormat() {
        Tache tache = new Tache();
        Membre membre = new Membre();
        EvaluationTache evaluation = new EvaluationTache(75, "Average", tache, membre);
        evaluation.setId(2L);
        String expected = "EvaluationTache [id=2, score=75, commentaire=Average, tache=" + tache + ", membre=" + membre + "]";
        assertEquals(expected, evaluation.toString());
    }

    @Test
    void toStringHandlesNullFields() {
        EvaluationTache evaluation = new EvaluationTache();
        evaluation.setId(3L);
        evaluation.setScore(0);
        String expected = "EvaluationTache [id=3, score=0, commentaire=null, tache=null, membre=null]";
        assertEquals(expected, evaluation.toString());
    }
}
