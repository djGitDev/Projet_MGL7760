package com.example.projet3.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

class TacheSequenceFactoryTest {

    @Test
    void createSequenceReturnsCorrectNumberOfTasksForProfessionalType() {
        TacheSequenceFactory factory = new TacheSequenceFactory();
        List<Tache> sequence = factory.createSequence("PROFESSIONNEL");
        assertEquals(7, sequence.size());
    }

    @Test
    void createSequenceReturnsCorrectNumberOfTasksForNonProfessionalType() {
        TacheSequenceFactory factory = new TacheSequenceFactory();
        List<Tache> sequence = factory.createSequence("NON_PROFESSIONNEL");
        assertEquals(7, sequence.size());
    }

    @Test
    void createSequenceAssignsCorrectScoresForProfessionalType() {
        TacheSequenceFactory factory = new TacheSequenceFactory();
        List<Tache> sequence = factory.createSequence("PROFESSIONNEL");
        assertEquals(18, sequence.get(0).getDureeEstimee());
    }

    @Test
    void createSequenceAssignsCorrectScoresForNonProfessionalType() {
        TacheSequenceFactory factory = new TacheSequenceFactory();
        List<Tache> sequence = factory.createSequence("NON_PROFESSIONNEL");
        assertEquals(10, sequence.get(2).getDureeEstimee());
    }

    @Test
    void createSequenceHandlesInvalidTypeGracefully() {
        TacheSequenceFactory factory = new TacheSequenceFactory();
        List<Tache> sequence = factory.createSequence("INVALID_TYPE");
        assertEquals(7, sequence.size());
    }
}
