package com.example.projet3.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class TacheTest {

    @Test
    void addEnfantAddsChildToList() {
        Tache parent = new Tache("Parent", 5);
        Tache child = new Tache("Child", 3);
        parent.addEnfant(child);
        assertTrue(parent.getEnfants().contains(child));
        assertEquals(parent, child.getParent());
    }

    @Test
    void removeEnfantRemovesChildFromList() {
        Tache parent = new Tache("Parent", 5);
        Tache child = new Tache("Child", 3);
        parent.addEnfant(child);
        parent.removeEnfant(child);
        assertFalse(parent.getEnfants().contains(child));
        assertNull(child.getParent());
    }


    @Test
    void setEtatToInProgressActivatesParent() {
        Tache parent = new Tache("Parent", 5);
        Tache child = new Tache("Child", 3);
        parent.addEnfant(child);
        parent.setEtat(EtatTache.PLANNED);
        child.setEtat(EtatTache.IN_PROGRESS);
        assertEquals(EtatTache.IN_PROGRESS, parent.getEtat());
    }

    @Test
    void checkAndActivateNextActivatesNextSibling() {
        Tache parent = new Tache("Parent", 5);
        Tache child1 = new Tache("Child", 3);
        Tache child2 = new Tache("Child", 4);
        parent.addEnfant(child1);
        parent.addEnfant(child2);
        child1.setEtat(EtatTache.DONE);
        parent.setEtat(EtatTache.IN_PROGRESS);
        child1.setEtat(EtatTache.DONE);
        assertEquals(EtatTache.IN_PROGRESS, child2.getEtat());
    }
}
