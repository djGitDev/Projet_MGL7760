package com.example.projet3.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MembreTest {

    @Test
    void defaultConstructorCreatesInstance() {
        Membre membre = new Membre();
        assertNotNull(membre);
    }

    @Test
    void parameterizedConstructorSetsFields() {
        Organisation organisation = new Organisation();
        LocalDate date = LocalDate.of(2023, 1, 1);
        Membre membre = new Membre("Doe", "John", TypeMembre.EMPLOYE, organisation, date);
        assertEquals("Doe", membre.getNom());
        assertEquals("John", membre.getPrenom());
        assertEquals(TypeMembre.EMPLOYE, membre.getType());
        assertEquals(organisation, membre.getOrganisation());
        assertEquals(date, membre.getDateAdhesion());
    }

    @Test
    void setIdAndGetId() {
        Membre membre = new Membre();
        membre.setId(1L);
        assertEquals(1L, membre.getId());
    }

    @Test
    void setNomAndGetNom() {
        Membre membre = new Membre();
        membre.setNom("Smith");
        assertEquals("Smith", membre.getNom());
    }

    @Test
    void setPrenomAndGetPrenom() {
        Membre membre = new Membre();
        membre.setPrenom("Jane");
        assertEquals("Jane", membre.getPrenom());
    }

    @Test
    void setTypeAndGetType() {
        Membre membre = new Membre();
        membre.setType(TypeMembre.ADMIN);
        assertEquals(TypeMembre.ADMIN, membre.getType());
    }

    @Test
    void setOrganisationAndGetOrganisation() {
        Membre membre = new Membre();
        Organisation organisation = new Organisation();
        membre.setOrganisation(organisation);
        assertEquals(organisation, membre.getOrganisation());
    }

    @Test
    void setDateAdhesionAndGetDateAdhesion() {
        Membre membre = new Membre();
        LocalDate date = LocalDate.of(2022, 5, 15);
        membre.setDateAdhesion(date);
        assertEquals(date, membre.getDateAdhesion());
    }

    @Test
    void setEvaluationsAndGetEvaluations() {
        Membre membre = new Membre();
        List<EvaluationTache> evaluations = new ArrayList<>();
        membre.setEvaluations(evaluations);
        assertEquals(evaluations, membre.getEvaluations());
    }

    @Test
    void setTachesAndGetTaches() {
        Membre membre = new Membre();
        List<Tache> taches = new ArrayList<>();
        membre.setTaches(taches);
        assertEquals(taches, membre.getTaches());
    }

    @Test
    void toStringReturnsCorrectFormat() {
        Organisation organisation = new Organisation();
        LocalDate date = LocalDate.of(2021, 10, 10);
        Membre membre = new Membre("Brown", "Alice", TypeMembre.VOLONTAIRE, organisation, date);
        membre.setId(2L);
        String expected = "Membre [id=2, nom=Brown, prenom=Alice, type=VOLONTAIRE, dateAdhesion=" + date + ", organisation=" + organisation + "]";
        assertEquals(expected, membre.toString());
    }

    @Test
    void toStringHandlesNullFields() {
        Membre membre = new Membre();
        membre.setId(3L);
        String expected = "Membre [id=3, nom=null, prenom=null, type=null, dateAdhesion=null, organisation=null]";
        assertEquals(expected, membre.toString());
    }

    @Test
    void getTachesReturnsInitializedList() {
        Membre membre = new Membre();
        assertNotNull(membre.getTaches());
        assertTrue(membre.getTaches().isEmpty());
    }
}
