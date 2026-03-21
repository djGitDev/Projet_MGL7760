package com.example.projet3.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import com.example.projet3.repository.EvaluationTacheRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.projet3.model.Membre;
import com.example.projet3.model.Organisation;
import com.example.projet3.repository.MembreRepository;
import com.example.projet3.repository.OrganisationRepository;

@ExtendWith(MockitoExtension.class)
class MembreServiceTest {
    @Mock
    OrganisationRepository organisationRepository;

    @Mock
    MembreRepository membreRepository;

    @Mock
    EvaluationTacheRepository evaluationTacheRepository;


    @InjectMocks
    MembreService membreService;

    @Test
    void ajouterMembreOrganisationNotFound() {
        Long organisationId = -1111L;
        assertThrows(RuntimeException.class, () -> {
            membreService.ajouterMembre(organisationId, null);
        });
    }

    @Test
    void ajouterMembre() {
        Long organisationId = 1L;
        Organisation organisation = new Organisation();
        organisation.setId(organisationId);
        Membre membre = new Membre();
        when(organisationRepository.findById(organisationId)).thenReturn(java.util.Optional.of(organisation));
        assertDoesNotThrow(() -> {
            membreService.ajouterMembre(organisationId, membre);
        });
    }

    @Test
    void getMembreByIdNotFound() {
        Long membreId = -1111L;
        assertThrows(RuntimeException.class, () -> {
            membreService.getMembreById(membreId);
        });
    }

    @Test
    void getMembreById() {
        Long membreId = 1L;
        Membre membre = new Membre();
        when(membreRepository.findById(membreId)).thenReturn(java.util.Optional.of(membre));
        assertDoesNotThrow(() -> {
            membreService.getMembreById(membreId);
        });
    }

    @Test
    void getAllMembres() {
        Membre membre1 = new Membre();
        Membre membre2 = new Membre();
        membreRepository.save(membre1);
        membreRepository.save(membre2);
        List<Membre> expectedMembres = List.of(membre1, membre2);
        when(membreRepository.findAll()).thenReturn(expectedMembres);
        List<Membre> membres = membreService.getAllMembres();
        assertEquals(2, membres.size());
        assertEquals(membres.get(0), membre1);
    }

    @Test
    void calculerScoreReturnsCorrectValue() {
        Long membreId = 1L;
        int expectedScore = 85;
        when(evaluationTacheRepository.sumScoreByMembreId(membreId)).thenReturn(expectedScore);

        int actualScore = membreService.calculerScore(membreId);

        assertEquals(expectedScore, actualScore);
    }

    @Test
    void getOrganisationByIdReturnsCorrectOrganisationId() {
        Long membreId = 1L;
        Long expectedOrganisationId = 10L;
        when(membreRepository.findOrganisation_IdById(membreId)).thenReturn(expectedOrganisationId);

        Long actualOrganisationId = membreService.getOrganisationById(membreId);

        assertEquals(expectedOrganisationId, actualOrganisationId);
    }

    @Test
    void getOrganisationByIdReturnsNullForNonExistentMembre() {
        Long membreId = -1L;
        when(membreRepository.findOrganisation_IdById(membreId)).thenReturn(null);

        Long actualOrganisationId = membreService.getOrganisationById(membreId);

        assertEquals(null, actualOrganisationId);
    }
}
