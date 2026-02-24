package com.example.projet3.service;

import com.example.projet3.model.Organisation;
import com.example.projet3.repository.OrganisationRepository;
import com.example.projet3.repository.EvaluationTacheRepository;
import com.example.projet3.repository.MembreRepository;
import com.example.projet3.repository.TacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganisationService {

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private MembreRepository membreRepository;

    @Autowired
    private TacheRepository tacheRepository;

    @Autowired
    private EvaluationTacheRepository evaluationTacheRepository;

    @Transactional
    public Organisation getOrganisationDetails(Long organisationId) {
        Organisation org = organisationRepository.findById(organisationId)
                .orElseThrow(() -> new RuntimeException("Organisation introuvable"));

        // Compte le nombre de membres dans l'organisation
        long nbMembres = membreRepository.countMembresByOrganisation(organisationId);

        // Calcule le score total de l'organisation
        int totalScore = evaluationTacheRepository.sumScoreByOrganisationId(organisationId);
        // Compte le nombre de tâches effectuées (supposons que l'état "DONE" signifie
        // la tâche effectuée)
        long nbTachesEffectuees = tacheRepository.countByOrganisationId(organisationId);

        // Utilisation des setters pour stocker ces valeurs dans l'entité (champs
        // transient)
        org.setNbMembres(nbMembres);
        org.setScoreTotal(totalScore);
        org.setNbTachesEffectuees(nbTachesEffectuees);

        return org;
    }
}
