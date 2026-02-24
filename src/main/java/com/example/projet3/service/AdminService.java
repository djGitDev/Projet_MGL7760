package com.example.projet3.service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projet3.model.*;

import com.example.projet3.repository.*;

import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class AdminService {
    @Autowired
    private OrganisationRepository organisationRepository;
    @Autowired
    private MembreRepository membreRepository;
    @Autowired
    private TacheRepository tacheRepository;
    @Autowired
    private OutilRepository outilRepository;
    @Autowired
    private Authorisation authorisationService;

    // Rechercher un membre par ID
    public Membre getMembreParId(Long adminId, Long membreId) throws AccessDeniedException {
        // Vérifie que l'adminId est bien un administrateur
        authorisationService.verifierAdmin(adminId);

        // Récupère l'organisation de l'admin
        Long organisationAdmin = membreRepository.findOrganisation_IdById(adminId);

        // Récupère le membre demandé
        Membre membre = membreRepository.findById(membreId)
                .orElseThrow(() -> new EntityNotFoundException("Membre non trouvé"));

        // Vérifie que ce membre appartient bien à la même organisation que l’admin
        if (!membre.getOrganisation().getId().equals(organisationAdmin)) {
            throw new AccessDeniedException("Ce membre ne fait pas partie de votre organisation.");
        }

        return membre;
    }

    // Lister les tâches de l'organisation
    public List<Tache> getTachesParOrganisation(Long organisationId) {
        return tacheRepository.findByOrganisationId(organisationId);
    }

    // Rechercher une tâche par ID
    public Tache getTacheAvecDetails(Long id) {
        return tacheRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tâche non trouvée"));
    }

    // Rechercher un outil par ID
    public Outil getOutilParId(Long id) {
        return outilRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Outil non trouvé"));
    }

    // Lister tous les outils disponibles
    public List<Outil> getOutilsDisponibles() {
        return outilRepository.findAll();
    }
}
