package com.example.projet3.controller;

import com.example.projet3.model.Membre;
import com.example.projet3.model.Outil;
import com.example.projet3.model.Tache;
import com.example.projet3.service.MembreService;
import com.example.projet3.service.OutilService;
import com.example.projet3.service.TacheService;
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
class MembreControllerTest {

    @Mock
    private TacheService tacheService;

    @Mock
    private MembreService membreService;

    @Mock
    private OutilService outilService;

    @InjectMocks
    private MembreController membreController;

    @Test
    void getTachesDuMembre_success() {
        Long membreId = 1L;
        List<Tache> taches = Arrays.asList(new Tache(), new Tache());
        when(tacheService.getTachesByMembre(membreId)).thenReturn(taches);
        List<Tache> result = membreController.getTachesDuMembre(membreId);
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tacheService).getTachesByMembre(membreId);
    }

    @Test
    void ajouterOutilATache_success() {
        Long tacheId = 1L;
        Long outilId = 2L;
        doNothing().when(tacheService).ajouterOutilATache(tacheId, outilId);
        membreController.ajouterOutilATache(tacheId, outilId);
        verify(tacheService).ajouterOutilATache(tacheId, outilId);
    }

    @Test
    void getOutilsDansTache_success() {
        Long tacheId = 1L;
        List<Outil> outils = Arrays.asList(new Outil(), new Outil());
        when(tacheService.getOutilsPourTache(tacheId)).thenReturn(outils);
        List<Outil> result = membreController.getOutilsDansTache(tacheId);
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tacheService).getOutilsPourTache(tacheId);
    }

    @Test
    void voirMesInfos_success() {
        Long membreId = 1L;
        Membre membre = new Membre();
        when(membreService.getMembreById(membreId)).thenReturn(membre);
        Membre result = membreController.voirMesInfos(membreId);
        assertNotNull(result);
        verify(membreService).getMembreById(membreId);
    }
}
