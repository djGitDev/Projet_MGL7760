package com.example.projet3.service;

import com.example.projet3.model.Organisation;
import com.example.projet3.repository.OrganisationRepository;
import com.example.projet3.repository.MembreRepository;
import com.example.projet3.repository.TacheRepository;
import com.example.projet3.repository.EvaluationTacheRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganisationServiceTest {

    @Mock
    private OrganisationRepository organisationRepository;

    @Mock
    private MembreRepository membreRepository;

    @Mock
    private TacheRepository tacheRepository;

    @Mock
    private EvaluationTacheRepository evaluationTacheRepository;

    @InjectMocks
    private OrganisationService organisationService;

    @Test
    void testGetOrganisationDetails() {

        Long organisationId = 1L;

        Organisation organisation = new Organisation();
        organisation.setId(organisationId);

        when(organisationRepository.findById(organisationId))
                .thenReturn(Optional.of(organisation));
        when(membreRepository.countMembresByOrganisation(organisationId))
                .thenReturn(5L);
        when(evaluationTacheRepository.sumScoreByOrganisationId(organisationId))
                .thenReturn(120);
        when(tacheRepository.countByOrganisationId(organisationId))
                .thenReturn(10L);

        Organisation result = organisationService.getOrganisationDetails(organisationId);
        assertNotNull(result);
        assertEquals(5L, result.getNbMembres());
        assertEquals(120, result.getScoreTotal());
        assertEquals(10L, result.getNbTachesEffectuees());

        verify(organisationRepository).findById(organisationId);
        verify(membreRepository).countMembresByOrganisation(organisationId);
        verify(evaluationTacheRepository).sumScoreByOrganisationId(organisationId);
        verify(tacheRepository).countByOrganisationId(organisationId);
    }
}