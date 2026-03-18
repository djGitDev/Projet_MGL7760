package com.example.projet3.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrganisationTest {

    @Test
    void defaultConstructorCreatesInstance() {
        Organisation organisation = new Organisation();
        assertNotNull(organisation);
    }

    @Test
    void parameterizedConstructorSetsFields() {
        Organisation organisation = new Organisation("Tech Corp", "Technology");
        assertEquals("Tech Corp", organisation.getNom());
        assertEquals("Technology", organisation.getType());
    }

    @Test
    void setIdAndGetId() {
        Organisation organisation = new Organisation();
        organisation.setId(1L);
        assertEquals(1L, organisation.getId());
    }

    @Test
    void setNomAndGetNom() {
        Organisation organisation = new Organisation();
        organisation.setNom("Health Inc.");
        assertEquals("Health Inc.", organisation.getNom());
    }

    @Test
    void setSecteurAndGetSecteur() {
        Organisation organisation = new Organisation();
        organisation.setType("Healthcare");
        assertEquals("Healthcare", organisation.getType());
    }

    @Test
    void setMembresAndGetMembres() {
        Organisation organisation = new Organisation();
        List<Membre> membres = new ArrayList<>();
        organisation.setMembres(membres);
        assertEquals(membres, organisation.getMembres());
    }

    @Test
    void toStringReturnsCorrectFormat() {
        Organisation organisation = new Organisation("Edu World", "Education");
        organisation.setId(2L);
        String expected = "Organisation [id=2, nom=Edu World, type=Education]";
        assertEquals(expected, organisation.toString());
    }

    @Test
    void toStringHandlesNullFields() {
        Organisation organisation = new Organisation();
        organisation.setId(3L);
        String expected = "Organisation [id=3, nom=null, type=null]";
        assertEquals(expected, organisation.toString());
    }

    @Test
    void getMembresReturnsUninitializedList() {
        Organisation organisation = new Organisation();
        assertNull(organisation.getMembres());
    }
}
