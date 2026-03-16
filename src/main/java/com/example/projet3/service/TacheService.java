
package com.example.projet3.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.projet3.model.*;
import com.example.projet3.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class TacheService {

    private final EvaluationTacheRepository evaluationTacheRepository;

    @Autowired
    private TacheRepository tacheRepository;

    @Autowired

    private OutilRepository outilRepository;

    TacheService(EvaluationTacheRepository evaluationTacheRepository) {
        this.evaluationTacheRepository = evaluationTacheRepository;
    }

    public List<Tache> getTachesByMembre(Long membreId) {
        return tacheRepository.findByMembres_Id(membreId);
    }

    // Récupérer toutes les tâches
    public List<Tache> getAllTaches() {
        return tacheRepository.findAll();
    }

    public void ajouterOutilATache(Long tacheId, Long outilId) {
        Tache tache = tacheRepository.findById(tacheId)
                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tâche introuvable"));
        Outil outil = outilRepository.findById(outilId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Outil non trouvé"));

        tache.addOutil(outil);
        ;
        tacheRepository.save(tache);
    }

    public List<Outil> getOutilsPourTache(Long tacheId) {
        Tache tache = tacheRepository.findById(tacheId)
                .orElseThrow(() -> new ResponseStatusException("Tâche introuvable"));
        return tache.getOutils();
    }

    public Tache getTacheDetails(Long tacheId) {
        return tacheRepository.findById(tacheId)
                .orElseThrow(() -> new ResponseStatusException("Tâche non trouvée"));
    }

    public List<Tache> getTachesByOrganisation(Long organisationId) {
        return tacheRepository.findByOrganisationId(organisationId);
    }

    public Tache getById(Long id) {
        return tacheRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tâche avec l'ID " + id + " non trouvée"));
    }

    public Tache changeEtat(Long id, EtatTache nouvelEtat) {
        Tache t = getById(id);
        t.setEtat(nouvelEtat);
        return tacheRepository.save(t);
    }

    public Tache ajoterSousTache(Long parentId, Tache enfant) {
        Tache parent = getById(parentId);
        enfant.setOrdre(parent.getEnfants().size());
        parent.addEnfant(enfant);
        tacheRepository.save(enfant);
        return parent;
    }

    public double calculateAvancement(Long id) {
        Tache t = getById(id);
        if (t.getEnfants().isEmpty()) {
            return t.getEtat() == EtatTache.DONE ? 100.0 : t.getEtat() == EtatTache.IN_PROGRESS ? 50.0 : 0.0;
        }
        return t.getEnfants().stream()
                .mapToDouble(child -> calculateAvancement(child.getId()))
                .average()
                .orElse(0.0);
    }

    public int calculateScore(Long id) {
        Tache t = getById(id);
        if (t.getEnfants().isEmpty()) {
            // feuille : score réel dans la table d’évaluation
            return evaluationTacheRepository.findByTacheId(id).stream()
                    .mapToInt(EvaluationTache::getScore)
                    .sum(); // ou moyenne selon ta logique
        }
        return t.getEnfants().stream()
                .mapToInt(child -> calculateScore(child.getId()))
                .sum();
    }

}
