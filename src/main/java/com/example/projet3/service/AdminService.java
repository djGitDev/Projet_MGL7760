package com.example.projet3.service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.projet3.model.*;
import com.example.projet3.repository.*;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AdminService {

    private final MembreRepository membreRepository;
    private final TacheRepository tacheRepository;
    private final OutilRepository outilRepository;
    private final Authorisation authorisationService;

    public AdminService(
            MembreRepository membreRepository,
            TacheRepository tacheRepository,
            OutilRepository outilRepository,
            Authorisation authorisationService) {
        this.membreRepository = membreRepository;
        this.tacheRepository = tacheRepository;
        this.outilRepository = outilRepository;
        this.authorisationService = authorisationService;
    }

    public Membre getMembreParId(Long adminId, Long membreId) throws AccessDeniedException {
        authorisationService.verifierAdmin(adminId);

        Long organisationAdmin = membreRepository.findOrganisation_IdById(adminId);

        Membre membre = membreRepository.findById(membreId)
                .orElseThrow(() -> new EntityNotFoundException("Membre non trouvé"));

        if (!membre.getOrganisation().getId().equals(organisationAdmin)) {
            throw new AccessDeniedException("Ce membre ne fait pas partie de votre organisation.");
        }

        return membre;
    }

    public List<Tache> getTachesParOrganisation(Long organisationId) {
        return tacheRepository.findByOrganisationId(organisationId);
    }

    public Tache getTacheAvecDetails(Long id) {
        return tacheRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tâche non trouvée"));
    }

    public Outil getOutilParId(Long id) {
        return outilRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Outil non trouvé"));
    }

    public List<Outil> getOutilsDisponibles() {
        return outilRepository.findAll();
    }
}