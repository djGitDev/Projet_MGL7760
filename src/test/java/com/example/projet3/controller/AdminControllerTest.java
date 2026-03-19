package com.example.projet3.controller;

import com.example.projet3.model.*;
import com.example.projet3.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private MembreService membreService;

    @Mock
    private OrganisationService organisationService;

    @Mock
    private AdminService adminService;

    @Mock
    private TacheService tacheService;

    @Mock
    private OutilService outilService;

    @Mock
    private Authorisation authorisation;

    @InjectMocks
    private AdminController adminController;

    @Test
    void afficherOrganisation_success() {
        Long membreId = 1L;
        Long organisationId = 2L;
        Organisation organisation = new Organisation("Org1", "Type1");
        when(membreService.getOrganisationById(membreId)).thenReturn(organisationId);
        when(organisationService.getOrganisationDetails(organisationId)).thenReturn(organisation);
        doNothing().when(authorisation).verifierAdmin(membreId);

        Organisation result = adminController.afficherOrganisation(membreId);

        assertNotNull(result);
        assertEquals("Org1", result.getNom());
        verify(authorisation).verifierAdmin(membreId);
        verify(membreService).getOrganisationById(membreId);
        verify(organisationService).getOrganisationDetails(organisationId);
    }

    @Test
    void afficherOrganisation_noOrganisationFound() {
        Long membreId = 1L;

        when(membreService.getOrganisationById(membreId)).thenReturn(null);
        doNothing().when(authorisation).verifierAdmin(membreId);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> adminController.afficherOrganisation(membreId));

        assertEquals("404 NOT_FOUND \"L'organisation associée à ce membre n'a pas été trouvée.\"", exception.getMessage());

        verify(authorisation).verifierAdmin(membreId);
        verify(membreService).getOrganisationById(membreId);
    }

    @Test
    void afficherMembre_success() throws AccessDeniedException {
        Long adminId = 1L;
        Long membreId = 2L;
        Membre membre = new Membre();

        when(adminService.getMembreParId(adminId, membreId)).thenReturn(membre);

        ResponseEntity<Membre> response = adminController.afficherMembre(adminId, membreId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(adminService).getMembreParId(adminId, membreId);
    }

    @Test
    void afficherMembre_accessDenied() throws AccessDeniedException {
        Long adminId = 1L;
        Long membreId = 2L;

        when(adminService.getMembreParId(adminId, membreId)).thenThrow(new AccessDeniedException("Access denied"));

        ResponseEntity<Membre> response = adminController.afficherMembre(adminId, membreId);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
        verify(adminService).getMembreParId(adminId, membreId);
    }

    @Test
    void getTachesParOrganisation_success() {
        Long organisationId = 1L;
        List<Tache> taches = Arrays.asList(new Tache(), new Tache());

        when(adminService.getTachesParOrganisation(organisationId)).thenReturn(taches);

        List<Tache> result = adminController.getTachesParOrganisation(organisationId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(adminService).getTachesParOrganisation(organisationId);
    }

    @Test
    void getTacheAvecDetails_success() {
        Long tacheId = 1L;
        Tache tache = new Tache();

        when(adminService.getTacheAvecDetails(tacheId)).thenReturn(tache);

        Tache result = adminController.getTacheAvecDetails(tacheId);

        assertNotNull(result);
        verify(adminService).getTacheAvecDetails(tacheId);
    }

    @Test
    void getTacheParId_notFound() {
        Long tacheId = 1L;

        when(tacheService.getById(tacheId)).thenReturn(null);

        ResponseEntity<String> response = adminController.getTacheParId(tacheId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Tâche non trouvée avec l'id 1", response.getBody());
        verify(tacheService).getById(tacheId);
    }
}
