package com.example.projet3.service;

import com.example.projet3.model.EtatTache;
import com.example.projet3.model.EvaluationTache;
import com.example.projet3.model.Outil;
import com.example.projet3.model.Tache;
import com.example.projet3.repository.EvaluationTacheRepository;
import com.example.projet3.repository.OutilRepository;
import com.example.projet3.repository.TacheRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TacheServiceTest {

    @Mock
    private TacheRepository tacheRepository;

    @Mock
    private OutilRepository outilRepository;

    @Mock
    private EvaluationTacheRepository evaluationTacheRepository;

    @InjectMocks
    private TacheService tacheService;

    @BeforeEach
    void setup() {

        MockitoAnnotations.openMocks(this);

        tacheService = new TacheService(evaluationTacheRepository);

        ReflectionTestUtils.setField(tacheService, "tacheRepository", tacheRepository);
        ReflectionTestUtils.setField(tacheService, "outilRepository", outilRepository);
    }

    @Test
    void testGetAllTaches() {
        List<Tache> taches = List.of(new Tache(), new Tache());
        when(tacheRepository.findAll()).thenReturn(taches);
        List<Tache> result = tacheService.getAllTaches();
        assertEquals(2, result.size());
        verify(tacheRepository).findAll();
    }

    @Test
    void getAllTachesReturnsEmptyListWhenNoTachesExist() {
        when(tacheRepository.findAll()).thenReturn(List.of());
        List<Tache> result = tacheService.getAllTaches();
        assertEquals(0, result.size());
    }

    @Test
    void testGetTachesByMembre() {
        Long membreId = 1L;
        List<Tache> taches = List.of(new Tache(), new Tache());
        when(tacheRepository.findByMembres_Id(membreId)).thenReturn(taches);
        List<Tache> result = tacheService.getTachesByMembre(membreId);
        assertEquals(2, result.size());
    }

    @Test
    void getTachesByMembreReturnsEmptyListWhenNoTachesExist() {
        Long membreId = 1L;
        when(tacheRepository.findByMembres_Id(membreId)).thenReturn(List.of());
        List<Tache> result = tacheService.getTachesByMembre(membreId);
        assertEquals(0, result.size());
    }

    @Test
    void testAjouterOutilATache() {
        Long tacheId = 1L;
        Long outilId = 2L;
        Tache tache = new Tache();
        Outil outil = new Outil();
        when(tacheRepository.findById(tacheId)).thenReturn(Optional.of(tache));
        when(outilRepository.findById(outilId)).thenReturn(Optional.of(outil));
        tacheService.ajouterOutilATache(tacheId, outilId);
        verify(tacheRepository).save(tache);
    }

    @Test
    void ajouterOutilATacheThrowsExceptionWhenTacheNotFound() {
        Long tacheId = 1L;
        Long outilId = 2L;
        when(tacheRepository.findById(tacheId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> tacheService.ajouterOutilATache(tacheId, outilId));
    }

    @Test
    void ajouterOutilATacheThrowsExceptionWhenOutilNotFound() {
        Long tacheId = 1L;
        Long outilId = 2L;
        Tache tache = new Tache();
        when(tacheRepository.findById(tacheId)).thenReturn(Optional.of(tache));
        when(outilRepository.findById(outilId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> tacheService.ajouterOutilATache(tacheId, outilId));
    }

    @Test
    void testChangeEtat() {
        Long id = 1L;
        Tache tache = new Tache();
        tache.setEtat(EtatTache.PLANNED);
        when(tacheRepository.findById(id)).thenReturn(Optional.of(tache));
        when(tacheRepository.save(any())).thenReturn(tache);
        Tache result = tacheService.changeEtat(id, EtatTache.DONE);

        assertEquals(EtatTache.DONE, result.getEtat());
    }

    @Test
    void changeEtatThrowsExceptionWhenTacheNotFound() {
        Long id = 1L;
        when(tacheRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> tacheService.changeEtat(id, EtatTache.DONE));
    }

    @Test
    void testCalculateAvancementTacheSimple() {
        Long id = 1L;
        Tache tache = new Tache();
        tache.setEtat(EtatTache.DONE);
        when(tacheRepository.findById(id)).thenReturn(Optional.of(tache));
        double result = tacheService.calculateAvancement(id);
        assertEquals(100.0, result);
    }

    @Test
    void calculateAvancementThrowsExceptionWhenTacheNotFound() {
        Long id = 1L;
        when(tacheRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> tacheService.calculateAvancement(id));
    }

    @Test
    void testCalculateScore() {
        Long tacheId = 1L;
        Tache tache = new Tache();
        EvaluationTache eval1 = new EvaluationTache();
        eval1.setScore(10);
        EvaluationTache eval2 = new EvaluationTache();
        eval2.setScore(20);
        when(tacheRepository.findById(tacheId)).thenReturn(Optional.of(tache));
        when(evaluationTacheRepository.findByTacheId(tacheId))
                .thenReturn(List.of(eval1, eval2));
        int score = tacheService.calculateScore(tacheId);
        assertEquals(30, score);
    }

    @Test
    void calculateScoreReturnsZeroWhenNoEvaluationsExist() {
        Long tacheId = 1L;
        Tache tache = new Tache();
        when(tacheRepository.findById(tacheId)).thenReturn(Optional.of(tache));
        when(evaluationTacheRepository.findByTacheId(tacheId)).thenReturn(List.of());
        int score = tacheService.calculateScore(tacheId);
        assertEquals(0, score);
    }

    @Test
    void calculateScoreThrowsExceptionWhenTacheNotFound() {
        Long tacheId = 1L;
        when(tacheRepository.findById(tacheId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> tacheService.calculateScore(tacheId));
    }

    @Test
    void testGetByIdNotFound() {
        Long id = 1L;
        when(tacheRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            tacheService.getById(id);
        });
    }
}
