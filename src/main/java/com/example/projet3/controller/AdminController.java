package com.example.projet3.controller;

import com.example.projet3.model.*;

import com.example.projet3.service.*;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MembreService membreService;

    @Autowired
    private OrganisationService organisationService;

    @Autowired

    private AdminService adminService;
    @Autowired

    private TacheService tacheService;

    @Autowired
    private OutilService outilService;

    @Autowired
    private Authorisation authorisation;

    // Avant chaque route une verification d'autorisation est faite si c'est
    // autoriser seulement pour un administrateur

    // Route pour afficher details d'organisation

    @GetMapping("/organisation")
    public Organisation afficherOrganisation(@RequestHeader("membreId") Long membreId) {
        // Vérifie que le membre est un administrateur
        authorisation.verifierAdmin(membreId); // Cette méthode va vérifier que le membre est admin
        Long organisationId = membreService.getOrganisationById(membreId);

        if (organisationId == null) {
            throw new RuntimeException("L'organisation associée à ce membre n'a pas été trouvée.");
        }

        System.out.println("Organisation ID récupéré pour le membre " + membreId + " : " + organisationId);

        Organisation organisation = organisationService.getOrganisationDetails(organisationId);
        return organisation;

    }

    // Route pour rechercher un membre par ID et afficher ses infos

    @GetMapping("/membres/{id}")
    public ResponseEntity<Membre> afficherMembre(
            @RequestHeader("membreId") Long adminId,
            @PathVariable Long membreId) {
        try {
            Membre membre = adminService.getMembreParId(adminId, membreId);
            return ResponseEntity.ok(membre);
        } catch (EntityNotFoundException | AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // ou .notFound() selon le cas
        }
    }

    // Route pour lister les tâches de l'organisation avec leurs états

    @GetMapping("/organisation/{organisationId}/taches")
    public List<Tache> getTachesParOrganisation(@PathVariable Long organisationId) {
        return adminService.getTachesParOrganisation(organisationId);
    }

    // Route pour rechercher une tâche par ID avec ses détails et outils

    @GetMapping("/taches/{id}")
    public Tache getTacheAvecDetails(@PathVariable Long id) {
        return adminService.getTacheAvecDetails(id);
    }

    // Route pour rechercher une tâche par ID avec ses détails et outils

    @GetMapping("/tache/{id}")
    public ResponseEntity<String> getTacheParId(@PathVariable Long id) {
        Tache tache = tacheService.getById(id);
        if (tache != null) {
            return ResponseEntity.ok(tache.toString());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tâche non trouvée avec l'id " + id);
        }
    }

    // Route rechercher un outil par ID

    @GetMapping("/outils/{id}")
    public Outil getOutilParId(@PathVariable Long id) {
        return adminService.getOutilParId(id);
    }

    // Route pour lister tous les outils disponibles

    @GetMapping("/outils")
    public List<Outil> getTousLesOutilsDisponibles() {
        return outilService.getOutilsDisponibles();
    }

    // Route pour ajouter une sous tache a une tache(on donne id de tache parent)

    @PostMapping("/{parentId}/sous-taches")
    public Tache addSous(@PathVariable Long parentId,
            @RequestBody Tache enfant) {
        return tacheService.ajoterSousTache(parentId, enfant);
    }

    // Route pour changer l'étata d'une tache

    @PatchMapping("/{id}/etat")
    public Tache updateEtat(@PathVariable Long id,
            @RequestParam EtatTache etat) {
        return tacheService.changeEtat(id, etat);
    }

    // Route pour retourner le score d'une tache

    @GetMapping("/{id}/score")
    public int score(@PathVariable Long id) {
        return tacheService.calculateScore(id);
    }
    // Route pour retourner l'avancement d'une tache

    @GetMapping("/{id}/avancement")
    public ResponseEntity<Double> getAvancement(@PathVariable Long id) {
        double pct = tacheService.calculateAvancement(id);
        return ResponseEntity.ok(pct);
    }
}
