package com.example.projet3.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

class TacheSequenceFactoryTest {

    @ParameterizedTest
    @CsvSource({
            "PROFESSIONNEL, 7",
            "NON_PROFESSIONNEL, 7",
            "INVALID_TYPE, 7"
    })
    void createSequenceReturnsCorrectNumberOfTasks(String type, int expectedSize) {
        TacheSequenceFactory factory = new TacheSequenceFactory();
        List<Tache> sequence = factory.createSequence(type);
        assertEquals(expectedSize, sequence.size());
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

}
