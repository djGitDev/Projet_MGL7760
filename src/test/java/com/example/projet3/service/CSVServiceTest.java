package com.example.projet3.service;

import com.example.projet3.model.*;
import com.example.projet3.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CSVServiceTest {

    @Mock
    private OrganisationRepository organisationRepository;

    @Mock
    private MembreRepository membreRepository;

    @Mock
    private TacheRepository tacheRepository;

    @Mock
    private OutilRepository outilRepository;

    @Mock
    private EvaluationTacheRepository evaluationTacheRepository;

    @Mock
    private RapportTacheRepository rapportTacheRepository;

    @InjectMocks
    private CSVService csvService;

    @TempDir
    private Path tempDir;

    @Test
    void importerOrganisationsSuccessfully() throws IOException {
        Path csvPath = tempDir.resolve("organisations.csv");
        String csvContent = """
                Nom de l'Organisation,Type d'Organisation
                Org1,Type1
                Org2,Type2
                """;
        Files.writeString(csvPath, csvContent);
        csvService.importerOrganisations(csvPath);
        verify(organisationRepository).saveAll(any());
    }

    @Test
    void importerOrganisationsWithEmptyFile() throws IOException {
        Path csvPath = tempDir.resolve("organisations_empty.csv");
        String csvContent = "Nom de l'Organisation,Type d'Organisation\n";
        Files.writeString(csvPath, csvContent);

        csvService.importerOrganisations(csvPath);
        verify(organisationRepository).saveAll(argThat(list -> {
            int count = 0;
            for (Object ignored : list) count++;
            return count == 0;
        }));
    }

    @Test
    void importerMembresSuccessfully() throws IOException {
        Path csvPath = tempDir.resolve("membres.csv");
        String csvContent = """
                Nom,Prénom,Type,Organisation ID,Date d'adhésion
                Dupont,Jean,ADMIN,1,2025-01-15
                Martin,Marie,VOLONTAIRE,1,2025-02-20
                """;
        Files.writeString(csvPath, csvContent);

        Organisation org = new Organisation("Test", "Type");
        org.setId(1L);
        when(organisationRepository.findById(1L)).thenReturn(Optional.of(org));
        csvService.importerMembres(csvPath);
        verify(membreRepository).saveAll(argThat(list -> {
            int count = 0;
            for (Object ignored : list) count++;
            return count == 2;
        }));
    }

    @Test
    void importerMembresWithInvalidOrganisationId() throws IOException {
        Path csvPath = tempDir.resolve("membres_invalid.csv");
        String csvContent = """
                Nom,Prénom,Type,Organisation ID,Date d'adhésion
                Dupont,Jean,ADMIN,999,2025-01-15
                """;
        Files.writeString(csvPath, csvContent);
        when(organisationRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> csvService.importerMembres(csvPath));
    }

    @Test
    void importerMembresWithEmptyFile() throws IOException {
        Path csvPath = tempDir.resolve("membres_empty.csv");
        String csvContent = "Nom,Prénom,Type,Organisation ID,Date d'adhésion\n";
        Files.writeString(csvPath, csvContent);
        csvService.importerMembres(csvPath);
        verify(membreRepository).saveAll(argThat(list -> !list.iterator().hasNext()));
    }

    @Test
    void importerTachesSuccessfully() throws IOException {
        Path csvPath = tempDir.resolve("taches.csv");
        String csvContent = """
                Nom de la Tâche,Type de Tâche,Description,Durée Estimée (heures),Organisation ID
                Tache1,BASIC,Description1,10,1
                Tache2,PROFESSIONNEL,Description2,20,1
                """;
        Files.writeString(csvPath, csvContent);
        Organisation org = new Organisation("Test", "Type");
        org.setId(1L);
        when(organisationRepository.findById(1L)).thenReturn(Optional.of(org));
        csvService.importerTaches(csvPath);
        verify(tacheRepository).saveAll(argThat(list -> {
            int count = 0;
            for (Object ignored : list) count++;
            return count == 2;
        }));
    }

    @Test
    void importerTachesWithInvalidOrganisationId() throws IOException {
        Path csvPath = tempDir.resolve("taches_invalid.csv");
        String csvContent = """
                Nom de la Tâche,Type de Tâche,Description,Durée Estimée (heures),Organisation ID
                Tache1,PROFESSIONNEL,Description1,10,999
                """;
        Files.writeString(csvPath, csvContent);
        when(organisationRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> csvService.importerTaches(csvPath));
    }

    @Test
    void importerTachesWithEmptyFile() throws IOException {
        Path csvPath = tempDir.resolve("taches_empty.csv");
        String csvContent = "Nom de la Tâche,Type de Tâche,Description,Durée Estimée (heures),Organisation ID\n";
        Files.writeString(csvPath, csvContent);
        csvService.importerTaches(csvPath);

        verify(tacheRepository).saveAll(argThat(list -> {
            int count = 0;
            for (Object ignored : list) count++;
            return count == 0;
        }));
    }

    @Test
    void importerOutilsSuccessfully() throws IOException {
        // Arrange
        Path csvPath = tempDir.resolve("outils.csv");
        String csvContent = """
                Nom de l'Outil,Type,Organisation ID,Disponibilité,Date d'Achat,Nombre d'Utilisations
                Outil1,TypeA,1,Disponible,2025-01-10,5
                Outil2,TypeB,1,Indisponible,2025-02-15,3
                """;
        Files.writeString(csvPath, csvContent);
        Organisation org = new Organisation("Test", "Type");
        org.setId(1L);
        when(organisationRepository.findById(1L)).thenReturn(Optional.of(org));

        csvService.importerOutils(csvPath);
        verify(outilRepository).saveAll(argThat(list -> {
            int count = 0;
            for (Object ignored : list) count++;
            return count == 2;
        }));
    }

    @Test
    void importerOutilsWithInvalidOrganisationId() throws IOException {
        Path csvPath = tempDir.resolve("outils_invalid.csv");
        String csvContent = """
                Nom de l'Outil,Type,Organisation ID,Disponibilité,Date d'Achat,Nombre d'Utilisations
                Outil1,TypeA,999,Disponible,2025-01-10,5
                """;
        Files.writeString(csvPath, csvContent);
        when(organisationRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> csvService.importerOutils(csvPath));
    }

    @Test
    void importerOutilsWithEmptyFile() throws IOException {
        Path csvPath = tempDir.resolve("outils_empty.csv");
        String csvContent = "Nom de l'Outil,Type,Organisation ID,Disponibilité,Date d'Achat,Nombre d'Utilisations\n";
        Files.writeString(csvPath, csvContent);
        csvService.importerOutils(csvPath);
        verify(outilRepository).saveAll(argThat(list -> {
            int count = 0;
            for (Object ignored : list) count++;
            return count == 0;
        }));
    }

    @Test
    void importerEvaluationsSuccessfully() throws IOException {
        Path csvPath = tempDir.resolve("evaluations.csv");
        String csvContent = """
                Tâche ID,Membre ID,Score,Commentaire
                1,1,85,Bon
                2,2,90,Excellent
                """;
        Files.writeString(csvPath, csvContent);

        Tache tache1 = new Tache();
        tache1.setId(1L);
        Tache tache2 = new Tache();
        tache2.setId(2L);
        Membre membre1 = new Membre();
        membre1.setId(1L);
        Membre membre2 = new Membre();
        membre2.setId(2L);

        when(tacheRepository.findById(1L)).thenReturn(Optional.of(tache1));
        when(tacheRepository.findById(2L)).thenReturn(Optional.of(tache2));
        when(membreRepository.findById(1L)).thenReturn(Optional.of(membre1));
        when(membreRepository.findById(2L)).thenReturn(Optional.of(membre2));

        csvService.importerEvaluations(csvPath);
        verify(evaluationTacheRepository).saveAll(argThat(list -> {
            int count = 0;
            for (Object ignored : list) count++;
            return count == 2;
        }));
    }

    @Test
    void importerEvaluationsWithInvalidTacheId() throws IOException {
        Path csvPath = tempDir.resolve("evaluations_invalid_tache.csv");
        String csvContent = """
                Tâche ID,Membre ID,Score,Commentaire
                999,1,85,Bon
                """;
        Files.writeString(csvPath, csvContent);
        when(tacheRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> csvService.importerEvaluations(csvPath));
    }

    @Test
    void importerEvaluationsWithInvalidMembreId() throws IOException {
        Path csvPath = tempDir.resolve("evaluations_invalid_membre.csv");
        String csvContent = """
                Tâche ID,Membre ID,Score,Commentaire
                1,999,85,Bon
                """;
        Files.writeString(csvPath, csvContent);
        Tache tache = new Tache();
        tache.setId(1L);
        when(tacheRepository.findById(1L)).thenReturn(Optional.of(tache));
        when(membreRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> csvService.importerEvaluations(csvPath));
    }

    @Test
    void importerEvaluationsWithEmptyFile() throws IOException {
        // Arrange
        Path csvPath = tempDir.resolve("evaluations_empty.csv");
        String csvContent = "Tâche ID,Membre ID,Score,Commentaire\n";
        Files.writeString(csvPath, csvContent);

        csvService.importerEvaluations(csvPath);
        verify(evaluationTacheRepository).saveAll(argThat(list -> {
            int count = 0;
            for (Object ignored : list) count++;
            return count == 0;
        }));
    }

    @Test
    void importerRapportsSuccessfully() throws IOException {
        Path csvPath = tempDir.resolve("rapports.csv");
        String csvContent = """
                Tâche ID,Membre ID,Date de Rapport,Commentaire,Etat
                1,1,2025-03-01,Rapport1,EN_COURS
                2,2,2025-03-02,Rapport2,TERMINE
                """;
        Files.writeString(csvPath, csvContent);

        Tache tache1 = new Tache();
        tache1.setId(1L);
        Tache tache2 = new Tache();
        tache2.setId(2L);
        Membre membre1 = new Membre();
        membre1.setId(1L);
        Membre membre2 = new Membre();
        membre2.setId(2L);

        when(tacheRepository.findById(1L)).thenReturn(Optional.of(tache1));
        when(tacheRepository.findById(2L)).thenReturn(Optional.of(tache2));
        when(membreRepository.findById(1L)).thenReturn(Optional.of(membre1));
        when(membreRepository.findById(2L)).thenReturn(Optional.of(membre2));

        csvService.importerRapports(csvPath);
        verify(rapportTacheRepository).saveAll(argThat(list -> {
            int count = 0;
            for (Object ignored : list) count++;
            return count == 2;
        }));
    }

    @Test
    void importerRapportsWithInvalidTacheId() throws IOException {
        Path csvPath = tempDir.resolve("rapports_invalid_tache.csv");
        String csvContent = """
                Tâche ID,Membre ID,Date de Rapport,Commentaire,Etat
                999,1,2025-03-01,Rapport1,EN_COURS
                """;
        Files.writeString(csvPath, csvContent);
        when(tacheRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> csvService.importerRapports(csvPath));
    }

    @Test
    void importerRapportsWithInvalidMembreId() throws IOException {
        Path csvPath = tempDir.resolve("rapports_invalid_membre.csv");
        String csvContent = """
                Tâche ID,Membre ID,Date de Rapport,Commentaire,Etat
                1,999,2025-03-01,Rapport1,EN_COURS
                """;
        Files.writeString(csvPath, csvContent);

        Tache tache = new Tache();
        tache.setId(1L);
        when(tacheRepository.findById(1L)).thenReturn(Optional.of(tache));
        when(membreRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> csvService.importerRapports(csvPath));
    }

    @Test
    void importerRapportsWithEmptyFile() throws IOException {
        Path csvPath = tempDir.resolve("rapports_empty.csv");
        String csvContent = "Tâche ID,Membre ID,Date de Rapport,Commentaire,Etat\n";
        Files.writeString(csvPath, csvContent);
        csvService.importerRapports(csvPath);

        verify(rapportTacheRepository).saveAll(argThat(list -> {
            int count = 0;
            for (Object ignored : list) count++;
            return count == 0;
        }));
    }

    @Test
    void importerTachesOutilSuccessfully() throws IOException {
        Path csvPath = tempDir.resolve("taches_outils.csv");
        String csvContent = """
                Tâche ID,Outil ID
                1,1
                1,2
                2,1
                """;
        Files.writeString(csvPath, csvContent);

        Tache tache1 = new Tache();
        tache1.setId(1L);
        tache1.setOutils(new ArrayList<>());
        Tache tache2 = new Tache();
        tache2.setId(2L);
        tache2.setOutils(new ArrayList<>());

        Outil outil1 = new Outil();
        outil1.setId(1L);
        Outil outil2 = new Outil();
        outil2.setId(2L);

        when(tacheRepository.findById(1L)).thenReturn(Optional.of(tache1));
        when(tacheRepository.findById(2L)).thenReturn(Optional.of(tache2));
        when(outilRepository.findById(1L)).thenReturn(Optional.of(outil1));
        when(outilRepository.findById(2L)).thenReturn(Optional.of(outil2));
        when(tacheRepository.findAll()).thenReturn(List.of(tache1, tache2));

        csvService.importerTachesOutild(csvPath);
        verify(tacheRepository, atLeastOnce()).save(any());
    }

    @Test
    void importerTachesOutilWithInvalidTacheId() throws IOException {
        Path csvPath = tempDir.resolve("taches_outils_invalid_tache.csv");
        String csvContent = """
                Tâche ID,Outil ID
                999,1
                """;
        Files.writeString(csvPath, csvContent);
        when(tacheRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> csvService.importerTachesOutild(csvPath));
    }

    @Test
    void importerTachesOutilWithInvalidOutilId() throws IOException {
        Path csvPath = tempDir.resolve("taches_outils_invalid_outil.csv");
        String csvContent = """
                Tâche ID,Outil ID
                1,999
                """;
        Files.writeString(csvPath, csvContent);

        Tache tache = new Tache();
        tache.setId(1L);
        when(tacheRepository.findById(1L)).thenReturn(Optional.of(tache));
        when(outilRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> csvService.importerTachesOutild(csvPath));
    }

    @Test
    void importerTachesOutilInitializesNullToolsList() throws IOException {
        Path csvPath = tempDir.resolve("taches_outils_null_list.csv");
        String csvContent = """
                Tâche ID,Outil ID
                1,1
                """;
        Files.writeString(csvPath, csvContent);

        Tache tache = new Tache();
        tache.setId(1L);
        tache.setOutils(null);

        Outil outil = new Outil();
        outil.setId(1L);

        when(tacheRepository.findById(1L)).thenReturn(Optional.of(tache));
        when(outilRepository.findById(1L)).thenReturn(Optional.of(outil));
        when(tacheRepository.findAll()).thenReturn(List.of(tache));

        csvService.importerTachesOutild(csvPath);

        assertNotNull(tache.getOutils());
        assertEquals(1, tache.getOutils().size());
        assertTrue(tache.getOutils().contains(outil));
    }

    @Test
    void importerTachesOutilWithEmptyFile() throws IOException {
        Path csvPath = tempDir.resolve("taches_outils_empty.csv");
        String csvContent = "Tâche ID,Outil ID\n";
        Files.writeString(csvPath, csvContent);

        when(tacheRepository.findAll()).thenReturn(new ArrayList<>());
        csvService.importerTachesOutild(csvPath);
        verify(tacheRepository).findAll();
    }
}
