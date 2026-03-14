package com.example.projet3.service;

import com.example.projet3.model.Membre;
import com.example.projet3.model.Organisation;
import com.example.projet3.model.Outil;
import com.example.projet3.model.Tache;
import com.example.projet3.repository.MembreRepository;
import com.example.projet3.repository.OrganisationRepository;
import com.example.projet3.repository.OutilRepository;
import com.example.projet3.repository.TacheRepository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private OrganisationRepository organisationRepository;

    @Mock
    private MembreRepository membreRepository;

    @Mock
    private TacheRepository tacheRepository;

    @Mock
    private OutilRepository outilRepository;

    @Mock
    private Authorisation authorisationService;

    @InjectMocks
    private AdminService adminService;

    @Test
    void getMembreParId_success() throws AccessDeniedException {

        // Arrange
        Long adminId = 1L;
        Long membreId = 2L;
        Long organisationId = 3L;
        Organisation organisation = new Organisation("OrgTest", "TypeTest");
        organisation.setId(organisationId);
        Membre membre = new Membre();
        membre.setId(membreId);
        membre.setOrganisation(organisation);

        when(membreRepository.findOrganisation_IdById(adminId)).thenReturn(organisationId);
        when(membreRepository.findById(membreId)).thenReturn(Optional.of(membre));
        doNothing().when(authorisationService).verifierAdmin(adminId);

        // Act
        Membre result = adminService.getMembreParId(adminId, membreId);

        // Assert
        assertNotNull(result);
        assertEquals(membreId, result.getId());
        assertEquals(organisationId, result.getOrganisation().getId());
        verify(authorisationService).verifierAdmin(adminId);
        verify(membreRepository).findById(membreId);
    }

    @Test
    void getMembreParId_accessDenied() {

        // Arrange
        Long adminId = 1L;
        Long membreId = 2L;
        Long organisationAdminId = 3L;
        Organisation orgAdmin = new Organisation("OrgAdmin", "TypeAdmin");
        orgAdmin.setId(organisationAdminId);
        Organisation orgMembre = new Organisation("OrgMembre", "TypeMembre");
        orgMembre.setId(4L);
        Membre membre = new Membre();
        membre.setId(membreId);
        membre.setOrganisation(orgMembre);

        when(membreRepository.findOrganisation_IdById(adminId)).thenReturn(organisationAdminId);
        when(membreRepository.findById(membreId)).thenReturn(Optional.of(membre));
        doNothing().when(authorisationService).verifierAdmin(adminId);

        // Act and Assert
        assertThrows(AccessDeniedException.class,
                () -> adminService.getMembreParId(adminId, membreId));
        verify(authorisationService).verifierAdmin(adminId);
        verify(membreRepository).findById(membreId);
    }

    @Test
    void getMembreParId_membreNotFound() {

        // Arrange
        Long adminId = 1L;
        Long membreId = 2L;

        doNothing().when(authorisationService).verifierAdmin(adminId);
        when(membreRepository.findById(membreId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class,
                () -> adminService.getMembreParId(adminId, membreId));

        verify(authorisationService).verifierAdmin(adminId);
        verify(membreRepository).findById(membreId);
    }

    @Test
    void getTachesParOrganisation() {
        Long organisationId = 1L;
        List<Tache> taches = Arrays.asList(new Tache(), new Tache());
        when(tacheRepository.findByOrganisationId(organisationId)).thenReturn(taches);
        List<Tache> result = adminService.getTachesParOrganisation(organisationId);
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tacheRepository).findByOrganisationId(organisationId);
    }

    @Test
    void getTacheAvecDetails_success() {
        Long tacheId = 1L;
        Tache tache = new Tache();
        tache.setId(tacheId);
        when(tacheRepository.findById(tacheId)).thenReturn(Optional.of(tache));
        Tache result = adminService.getTacheAvecDetails(tacheId);
        assertNotNull(result);
        assertEquals(tacheId, result.getId());
        verify(tacheRepository).findById(tacheId);
    }

    @Test
    void getTacheAvecDetails_notFound() {
        Long tacheId = 1L;
        when(tacheRepository.findById(tacheId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> adminService.getTacheAvecDetails(tacheId));
        verify(tacheRepository).findById(tacheId);
    }

    @Test
    void getOutilParId_success() {
        Long outilId = 1L;
        Outil outil = new Outil();
        outil.setId(outilId);
        when(outilRepository.findById(outilId)).thenReturn(Optional.of(outil));
        Outil result = adminService.getOutilParId(outilId);
        assertNotNull(result);
        assertEquals(outilId, result.getId());
        verify(outilRepository).findById(outilId);
    }

    @Test
    void getOutilParId_notFound() {
        Long outilId = 1L;
        when(outilRepository.findById(outilId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> adminService.getOutilParId(outilId));
        verify(outilRepository).findById(outilId);
    }

    @Test
    void getOutilsDisponibles() {
        List<Outil> outils = Arrays.asList(new Outil(), new Outil());
        when(outilRepository.findAll()).thenReturn(outils);
        List<Outil> result = adminService.getOutilsDisponibles();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(outilRepository).findAll();
    }
}