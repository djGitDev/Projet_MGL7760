package com.example.projet3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projet3.model.Membre;
import com.example.projet3.model.Outil;
import com.example.projet3.model.Tache;
import com.example.projet3.repository.MembreRepository;
import com.example.projet3.service.MembreService;
import com.example.projet3.service.OutilService;
import com.example.projet3.service.TacheService;

@RestController
@RequestMapping("/member")
public class MembreController {
    @Autowired
    private TacheService tacheService;
    @Autowired
    private MembreService membreService;
    @Autowired
    private OutilService outilService;

    // Voir la liste de ses tâches
    @GetMapping("/{id}/taches")
    public List<Tache> getTachesDuMembre(@PathVariable Long id) {
        return tacheService.getTachesByMembre(id);
    }

    // Ajouter un outil à une tâche
    @PostMapping("/taches/{tacheId}/outils/{outilId}")
    public void ajouterOutilATache(@PathVariable Long tacheId, @PathVariable Long outilId) {
        tacheService.ajouterOutilATache(tacheId, outilId);
    }

    // Voir la liste des outils utilisés dans une tâche
    @GetMapping("/taches/{tacheId}/outils")
    public List<Outil> getOutilsDansTache(@PathVariable Long tacheId) {
        return tacheService.getOutilsPourTache(tacheId);
    }

    /**
     * Terminer une tâche (changer son état)
     * @PostMapping("/taches/{id}/terminer")
     * public void terminerTache(@PathVariable Long id) {
     * tacheService.terminerTache(id);
     * }
     * 
     * // Ajouter un commentaire à une tâche
     * @PostMapping("/taches/{id}/commentaire")
     * public void ajouterCommentaire(@PathVariable Long id, @RequestBody String
     * commentaire) {
     * tacheService.ajouterCommentaire(id, commentaire);
     * }
     */
    // Voir ses informations et son score
    @GetMapping("/{id}")
    public Membre voirMesInfos(@PathVariable Long id) {
        return membreService.getMembreById(id);
    }

}
