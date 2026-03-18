package com.example.projet3.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.projet3.model.Outil;
import com.example.projet3.repository.OutilRepository;

@ExtendWith(MockitoExtension.class)
class OutilServiceTest {

    @Mock
    private OutilRepository outilRepository;

    @InjectMocks
    private OutilService outilService;

    @Test
    void getOutilByIdReturnsOutilWhenFound() {
        Long id = 1L;
        Outil outil = new Outil();
        outil.setId(id);
        when(outilRepository.findById(id)).thenReturn(Optional.of(outil));

        Outil result = outilService.getOutilById(id);

        assertEquals(id, result.getId());
        verify(outilRepository, times(1)).findById(id);
    }

    @Test
    void getOutilsDisponiblesReturnsListOfAvailableOutils() {
        Outil outil1 = new Outil();
        outil1.setDisponibilite("Disponible");
        Outil outil2 = new Outil();
        outil2.setDisponibilite("Disponible");
        List<Outil> outilsDisponibles = Arrays.asList(outil1, outil2);
        when(outilRepository.findByDisponibilite("Disponible")).thenReturn(outilsDisponibles);

        List<Outil> result = outilService.getOutilsDisponibles();

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(o -> "Disponible".equals(o.getDisponibilite())));
        verify(outilRepository, times(1)).findByDisponibilite("Disponible");
    }

    @Test
    void getOutilByIdThrowsExceptionWhenNotFound() {
        Long id = -1111L;
        when(outilRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> outilService.getOutilById(id));
        verify(outilRepository, times(1)).findById(id);
    }

    @Test
    void getOutilsDisponiblesReturnsEmptyListWhenNoAvailableOutils() {
        when(outilRepository.findByDisponibilite("Disponible")).thenReturn(Arrays.asList());

        List<Outil> result = outilService.getOutilsDisponibles();

        assertTrue(result.isEmpty());
        verify(outilRepository, times(1)).findByDisponibilite("Disponible");
    }
}
