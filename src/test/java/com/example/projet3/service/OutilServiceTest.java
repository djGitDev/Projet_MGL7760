package com.example.projet3.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.projet3.repository.OutilRepository;

@ExtendWith(MockitoExtension.class)
class OutilServiceTest {

    @Mock
    private OutilRepository outilRepository;

    @InjectMocks
    private OutilService outilService;

    @Test
    void testGetOutilByIdNotFound() {
        Long id = -1111L;
        assertThrows(RuntimeException.class, () -> {
            outilService.getOutilById(id);
        });
    }
}
