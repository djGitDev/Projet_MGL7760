package com.example.projet3.controller;

import com.example.projet3.model.Outil;
import com.example.projet3.repository.OutilRepository;
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
class OutilControllerTest {

    @Mock
    private OutilRepository outilRepository;

    @InjectMocks
    private OutilController outilController;

    @Test
    void getOutils() {
        List<Outil> outils = Arrays.asList(new Outil(), new Outil());
        when(outilRepository.findAll()).thenReturn(outils);
        List<Outil> result = outilController.getOutils();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(outilRepository).findAll();
    }

    @Test
    void getOutilsZeroOutil() {
        when(outilRepository.findAll()).thenReturn(Arrays.asList());
        List<Outil> result = outilController.getOutils();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(outilRepository).findAll();
    }
}
