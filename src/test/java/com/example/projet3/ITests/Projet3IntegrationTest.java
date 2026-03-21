package com.example.projet3.ITests;

import com.example.projet3.model.*;
import com.example.projet3.repository.*;
import com.example.projet3.service.OutilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class Projet3IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TacheRepository tacheRepository;

    @Autowired
    private MembreRepository membreRepository;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private OutilRepository outilRepository;

    @MockitoBean
    private OutilService outilService;

    @Autowired
    private RapportTacheRepository rapportTacheRepository;

    @Autowired
    private EvaluationTacheRepository evaluationTacheRepository;

    private Membre membreTest;
    private Organisation organisationTest;
    private Tache tacheTest;
    private Outil outilTest;

    @BeforeEach
    void setup() {
        rapportTacheRepository.deleteAll();
        evaluationTacheRepository.deleteAll();
        tacheRepository.deleteAll();
        outilRepository.deleteAll();
        membreRepository.deleteAll();
        organisationRepository.deleteAll();


        tacheTest = new Tache();
        tacheTest.setNom("Tâche Test");
        tacheTest.setEtat(EtatTache.PLANNED);
        tacheTest.setOrganisation(organisationTest);
        tacheTest = tacheRepository.save(tacheTest);

        outilTest = new Outil();
        outilTest.setNom("Outil Test");
        outilTest.setDisponibilite("disponible");
        outilTest = outilRepository.save(outilTest);

        List<Outil> outilsMock = List.of(outilTest);
        Mockito.when(outilService.getOutilsDisponibles()).thenReturn(outilsMock);
    }

    @Test
    void affichageDesTachesDunMembre_retourneListeTaches() throws Exception {
        organisationTest = new Organisation();
        organisationTest.setNom("Organisation Test");
        organisationTest.setType("TEST");
        organisationTest = organisationRepository.save(organisationTest);

        membreTest = new Membre();
        membreTest.setNom("Admin");
        membreTest.setOrganisation(organisationTest);
        membreTest.setType(TypeMembre.ADMIN);
        membreTest = membreRepository.save(membreTest);
        mockMvc.perform(get("/member/" + membreTest.getId() + "/taches"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void ajoutDunOutilATache_ajouteOutilAvecSucces() throws Exception {
        mockMvc.perform(post("/member/taches/" + tacheTest.getId() + "/outils/" + outilTest.getId()))
                .andExpect(status().isOk());
    }


    @Test
    void changementEtatTache_changeEtatAvecSucces() throws Exception {
        mockMvc.perform(patch("/admin/" + tacheTest.getId() + "/etat")
                        .param("etat", EtatTache.DONE.name()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void recuperationListeOutilsDisponibles_retourneListeOutils() throws Exception {
        mockMvc.perform(get("/admin/outils"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }
}