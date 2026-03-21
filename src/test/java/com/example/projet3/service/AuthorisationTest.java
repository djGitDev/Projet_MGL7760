package com.example.projet3.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.example.projet3.model.Membre;
import com.example.projet3.model.TypeMembre;
import com.example.projet3.repository.MembreRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AuthorisationTest {

    @Mock
    private MembreRepository membreRepository;

    @Mock
    private MembreService membreService;

    @InjectMocks
    private Authorisation authorisation;

    @Test
    void testVerifierTypeMembreNull() {
        long membreId = 1L;
        TypeMembre typeRequis = TypeMembre.ADMIN;
        when(membreService.getMembreById(membreId)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> {
            authorisation.verifierType(membreId, typeRequis,membreService);
        });
    }

    @Test
    void testVerifierTypeMembreWrongType() {
        long membreId = 1L;
        TypeMembre typeRequis = TypeMembre.ADMIN;
        Membre membre = new Membre();
        membre.setType(TypeMembre.EMPLOYE);
        when(membreService.getMembreById(membreId)).thenReturn(membre);
        assertThrows(ResponseStatusException.class, () -> {
            authorisation.verifierType(membreId, typeRequis,membreService);
        });
    }

    @Test
    void testVerifierTypeMembreGoodType() {
        long membreId = 1L;
        TypeMembre typeRequis = TypeMembre.ADMIN;
        Membre membre = new Membre();
        membre.setType(TypeMembre.ADMIN);
        when(membreService.getMembreById(membreId)).thenReturn(membre);
        assertDoesNotThrow(() -> {
            authorisation.verifierType(membreId, typeRequis,membreService);
        });
    }

    @Test
    void testVerifierUnDesTypesMembreNull() {
        long membreId = 1L;
        List<TypeMembre> typeMembres = new ArrayList<>();
        when(membreService.getMembreById(membreId)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> {
            authorisation.verifierUnDesTypes(membreId, typeMembres);
        });
    }

    @Test
    void testVerifierUnDesTypesInvalidMembre() {
        long membreId = 1L;
        Membre membre = new Membre();
        membre.setType(TypeMembre.EMPLOYE);
        List<TypeMembre> typeMembres = new ArrayList<>();
        when(membreService.getMembreById(membreId)).thenReturn(membre);
        assertThrows(ResponseStatusException.class, () -> {
            authorisation.verifierUnDesTypes(membreId, typeMembres);
        });
    }

    @Test
    void testVerifierUnDesTypes() {
        long membreId = 1L;
        Membre membre = new Membre();
        membre.setType(TypeMembre.EMPLOYE);
        List<TypeMembre> typeMembres = List.of(TypeMembre.EMPLOYE);
        when(membreService.getMembreById(membreId)).thenReturn(membre);
        assertDoesNotThrow(() -> {
            authorisation.verifierUnDesTypes(membreId, typeMembres);
        });
    }

}
