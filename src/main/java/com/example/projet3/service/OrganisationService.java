package com.example.projet3.service;

import com.example.projet3.model.Organisation;
import com.example.projet3.repository.OrganisationRepository;
import com.example.projet3.repository.EvaluationTacheRepository;
import com.example.projet3.repository.MembreRepository;
import com.example.projet3.repository.TacheRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganisationService {

    private final OrganisationRepository organisationRepository;
    private final MembreRepository membreRepository;
    private final TacheRepository tacheRepository;
    private final EvaluationTacheRepository evaluationTacheRepository;

    public OrganisationService(
            OrganisationRepository organisationRepository,
            MembreRepository membreRepository,
            TacheRepository tacheRepository,
            EvaluationTacheRepository evaluationTacheRepository) {

        this.organisationRepository = organisationRepository;
        this.membreRepository = membreRepository;
        this.tacheRepository = tacheRepository;
        this.evaluationTacheRepository = evaluationTacheRepository;
    }

    @Transactional
    public Organisation getOrganisationDetails(Long organisationId) {

        Organisation org = organisationRepository.findById(organisationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Organisation introuvable"));

        long nbMembres = membreRepository.countMembresByOrganisation(organisationId);

        int totalScore = evaluationTacheRepository.sumScoreByOrganisationId(organisationId);

        long nbTachesEffectuees = tacheRepository.countByOrganisationId(organisationId);

        org.setNbMembres(nbMembres);
        org.setScoreTotal(totalScore);
        org.setNbTachesEffectuees(nbTachesEffectuees);

        return org;
    }
}