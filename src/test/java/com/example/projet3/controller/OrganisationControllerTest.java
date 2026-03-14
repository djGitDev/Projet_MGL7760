package com.example.projet3.controller;

import com.example.projet3.model.Organisation;
import com.example.projet3.repository.OrganisationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganisationControllerTest {

    @Mock
    private OrganisationRepository organisationRepository;

    @InjectMocks
    private OrganisationController organisationController;

    @Test
    void getOrganisations_returnsAllOrganisations() {
        List<Organisation> organisations = Arrays.asList(new Organisation(), new Organisation());

        when(organisationRepository.findAll()).thenReturn(organisations);

        List<Organisation> result = organisationController.getOrganisations();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(organisationRepository).findAll();
    }

    @Test
    void getOrganisations_returnsEmptyListWhenNoOrganisations() {
        when(organisationRepository.findAll()).thenReturn(Arrays.asList());

        List<Organisation> result = organisationController.getOrganisations();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(organisationRepository).findAll();
    }
}
