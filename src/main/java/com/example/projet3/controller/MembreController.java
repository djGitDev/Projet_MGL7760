package com.example.projet3.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projet3.model.Membre;
import com.example.projet3.model.Outil;
import com.example.projet3.model.Tache;
import com.example.projet3.service.MembreService;
import com.example.projet3.service.TacheService;

@RestController
@RequestMapping("/member")
public class MembreController {

    private final TacheService tacheService;
    private final MembreService membreService;

    public MembreController(TacheService tacheService, MembreService membreService) {
        this.tacheService = tacheService;
        this.membreService = membreService;
    }

    @GetMapping("/{id}/taches")
    public List<Tache> getTachesDuMembre(@PathVariable Long id) {
        return tacheService.getTachesByMembre(id);
    }

    @PostMapping("/taches/{tacheId}/outils/{outilId}")
    public void ajouterOutilATache(@PathVariable Long tacheId, @PathVariable Long outilId) {
        tacheService.ajouterOutilATache(tacheId, outilId);
    }

    @GetMapping("/taches/{tacheId}/outils")
    public List<Outil> getOutilsDansTache(@PathVariable Long tacheId) {
        return tacheService.getOutilsPourTache(tacheId);
    }

    @GetMapping("/{id}")
    public Membre voirMesInfos(@PathVariable Long id) {
        return membreService.getMembreById(id);
    }
}