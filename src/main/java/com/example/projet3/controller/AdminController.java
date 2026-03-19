package com.example.projet3.controller;

import com.example.projet3.model.*;
import com.example.projet3.service.*;

import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final MembreService membreService;
    private final OrganisationService organisationService;
    private final AdminService adminService;
    private final TacheService tacheService;
    private final OutilService outilService;
    private final Authorisation authorisation;

    public AdminController(
            MembreService membreService,
            OrganisationService organisationService,
            AdminService adminService,
            TacheService tacheService,
            OutilService outilService,
            Authorisation authorisation) {
        this.membreService = membreService;
        this.organisationService = organisationService;
        this.adminService = adminService;
        this.tacheService = tacheService;
        this.outilService = outilService;
        this.authorisation = authorisation;
    }

    @GetMapping("/organisation")
    public Organisation afficherOrganisation(@RequestHeader("membreId") Long membreId) {
        authorisation.verifierAdmin(membreId);
        Long organisationId = membreService.getOrganisationById(membreId);

        if (organisationId == null) {
          throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "L'organisation associée à ce membre n'a pas été trouvée.");
        }

        logger.info("Organisation ID récupéré pour le membre {} : {}", membreId, organisationId);

        return organisationService.getOrganisationDetails(organisationId);
    }

    @GetMapping("/membres/{id}")
    public ResponseEntity<Membre> afficherMembre(
            @RequestHeader("membreId") Long adminId,
            @PathVariable Long membreId) {
        try {
            Membre membre = adminService.getMembreParId(adminId, membreId);
            return ResponseEntity.ok(membre);
        } catch (EntityNotFoundException | AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/organisation/{organisationId}/taches")
    public List<Tache> getTachesParOrganisation(@PathVariable Long organisationId) {
        return adminService.getTachesParOrganisation(organisationId);
    }

    @GetMapping("/taches/{id}")
    public Tache getTacheAvecDetails(@PathVariable Long id) {
        return adminService.getTacheAvecDetails(id);
    }

    @GetMapping("/tache/{id}")
    public ResponseEntity<String> getTacheParId(@PathVariable Long id) {
        Tache tache = tacheService.getById(id);
        if (tache != null) {
            return ResponseEntity.ok(tache.toString());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tâche non trouvée avec l'id " + id);
        }
    }

    @GetMapping("/outils/{id}")
    public Outil getOutilParId(@PathVariable Long id) {
        return adminService.getOutilParId(id);
    }

    @GetMapping("/outils")
    public List<Outil> getTousLesOutilsDisponibles() {
        return outilService.getOutilsDisponibles();
    }

    @PostMapping("/{parentId}/sous-taches")
    public Tache addSous(@PathVariable Long parentId, @RequestBody Tache enfant) {
        return tacheService.ajoterSousTache(parentId, enfant);
    }

    @PatchMapping("/{id}/etat")
    public Tache updateEtat(@PathVariable Long id, @RequestParam EtatTache etat) {
        return tacheService.changeEtat(id, etat);
    }

    @GetMapping("/{id}/score")
    public int score(@PathVariable Long id) {
        return tacheService.calculateScore(id);
    }

    @GetMapping("/{id}/avancement")
    public ResponseEntity<Double> getAvancement(@PathVariable Long id) {
        double pct = tacheService.calculateAvancement(id);
        return ResponseEntity.ok(pct);
    }
}