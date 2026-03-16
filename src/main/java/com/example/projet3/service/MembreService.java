package com.example.projet3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.projet3.model.*;
import com.example.projet3.repository.*;

@Service

public class MembreService {
    @Autowired
    private MembreRepository membreRepository;

    @Autowired
    private OrganisationRepository organisationRepository;
    @Autowired
    private EvaluationTacheRepository evaluationTacheRepository;

    public int calculerScore(Long membreId) {
        return evaluationTacheRepository.sumScoreByMembreId(membreId);
    }

    public Membre ajouterMembre(Long organisationId, Membre membre) {
        Organisation organisation = organisationRepository.findById(organisationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organisation non trouvée"));
        membre.setOrganisation(organisation);
        return membreRepository.save(membre);
    }

    public Membre getMembreById(Long id) {
        return membreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membre non trouvé"));
    }

    public Long getOrganisationById(Long idMembre) {
        return membreRepository.findOrganisation_IdById(idMembre);

    }

    public List<Membre> getAllMembres() {
        return membreRepository.findAll();
    }

}
