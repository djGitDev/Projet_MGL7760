package com.example.projet3.service;

import com.example.projet3.model.*;
import com.example.projet3.repository.*;

import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVService {

    private static final Logger logger = LoggerFactory.getLogger(CSVService.class);

    private final OrganisationRepository organisationRepository;
    private final MembreRepository membreRepository;
    private final TacheRepository tacheRepository;
    private final OutilRepository outilRepository;
    private final EvaluationTacheRepository evaluationTacheRepository;
    private final RapportTacheRepository rapportTacheRepository;

    public CSVService(
            OrganisationRepository organisationRepository,
            MembreRepository membreRepository,
            TacheRepository tacheRepository,
            OutilRepository outilRepository,
            EvaluationTacheRepository evaluationTacheRepository,
            RapportTacheRepository rapportTacheRepository) {
        this.organisationRepository = organisationRepository;
        this.membreRepository = membreRepository;
        this.tacheRepository = tacheRepository;
        this.outilRepository = outilRepository;
        this.evaluationTacheRepository = evaluationTacheRepository;
        this.rapportTacheRepository = rapportTacheRepository;
    }

    public void importerTaches(Path cheminFichier) throws IOException {
        List<Tache> taches = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier.toFile()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord record : csvParser) {
                String nom = record.get("Nom de la Tâche");
                String typeString = record.get("Type de Tâche");
                TypeTache type = TypeTache.valueOf(typeString.toUpperCase());
                String description = record.get("Description");
                int dureeEstimee = Integer.parseInt(record.get("Durée Estimée (heures)"));
                Long organisationId = Long.parseLong(record.get("Organisation ID"));

                Organisation organisation = organisationRepository.findById(organisationId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Organisation non trouvée avec l'ID : " + organisationId));

                int ordre = Sequence.fromLibelle(nom)
                        .map(Sequence::ordinal)
                        .map(i -> i + 1)
                        .orElse(0);

                taches.add(new Tache(nom, type, description, dureeEstimee, organisation, ordre));
            }
        }

        tacheRepository.saveAll(taches);
        logger.info("Importation des tâches terminée avec succès.");
    }

    public void importerOrganisations(Path cheminFichier) throws IOException {
        List<Organisation> organisations = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(cheminFichier);
             CSVParser csvParser = new CSVParser(
                     reader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            for (CSVRecord record : csvParser) {
                String nom = record.get("Nom de l'Organisation");
                String type = record.get("Type d'Organisation");
                organisations.add(new Organisation(nom, type));
            }
        }

        organisationRepository.saveAll(organisations);
        logger.info("Importation des organisations terminée avec succès.");
    }

    public void importerMembres(Path cheminFichier) throws IOException {
        List<Membre> membres = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier.toFile()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord record : csvParser) {
                String nom = record.get("Nom");
                String prenom = record.get("Prénom");
                String typeMember = record.get("Type");
                TypeMembre type = TypeMembre.valueOf(typeMember.toUpperCase());
                Long organisationId = Long.parseLong(record.get("Organisation ID"));
                LocalDate dateAdhesion = LocalDate.parse(record.get("Date d'adhésion"), formatter);

                Organisation organisation = organisationRepository.findById(organisationId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Organisation non trouvée avec l'ID : " + organisationId));

                membres.add(new Membre(nom, prenom, type, organisation, dateAdhesion));
            }
        }

        membreRepository.saveAll(membres);
        logger.info("Importation des membres terminée avec succès.");
    }

    public void importerOutils(Path cheminFichier) throws IOException {
        List<Outil> outils = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(cheminFichier);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord record : csvParser) {
                String nom = record.get("Nom de l'Outil");
                String type = record.get("Type");
                Long organisationId = Long.parseLong(record.get("Organisation ID"));
                String disponibilite = record.get("Disponibilité");
                String dateAchat = record.get("Date d'Achat");
                int nombre = Integer.parseInt(record.get("Nombre d'Utilisations"));

                Organisation organisation = organisationRepository.findById(organisationId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Organisation non trouvée avec l'ID : " + organisationId));

                outils.add(new Outil(nom, type, disponibilite, dateAchat, nombre, organisation));
            }
        }

        outilRepository.saveAll(outils);
        logger.info("Importation des outils terminée avec succès.");
    }

    public void importerEvaluations(Path cheminFichier) throws IOException {
        List<EvaluationTache> evaluationTaches = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(cheminFichier);
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : parser) {
                long tacheId = Long.parseLong(record.get("Tâche ID"));
                long membreId = Long.parseLong(record.get("Membre ID"));
                int score = Integer.parseInt(record.get("Score"));
                String commentaire = record.get("Commentaire");

                Tache tache = tacheRepository.findById(tacheId)
                        .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable : " + tacheId));
                Membre membre = membreRepository.findById(membreId)
                        .orElseThrow(() -> new EntityNotFoundException("Membre introuvable : " + membreId));

                EvaluationTache evaluation = new EvaluationTache(score, commentaire, tache, membre);
                evaluationTaches.add(evaluation);
            }
        }

        evaluationTacheRepository.saveAll(evaluationTaches);
        logger.info("Importation des évaluations terminée avec succès.");
    }

    public void importerRapports(Path cheminFichier) throws IOException {
        List<RapportTache> rapportTaches = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(cheminFichier);
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : parser) {
                long tacheId = Long.parseLong(record.get("Tâche ID"));
                long membreId = Long.parseLong(record.get("Membre ID"));
                LocalDate date = LocalDate.parse(record.get("Date de Rapport"));
                String commentaire = record.get("Commentaire");
                String etat = record.get("Etat");

                Tache tache = tacheRepository.findById(tacheId)
                        .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable : " + tacheId));
                Membre membre = membreRepository.findById(membreId)
                        .orElseThrow(() -> new EntityNotFoundException("Membre introuvable : " + membreId));

                RapportTache rapport = new RapportTache(date, commentaire, etat, tache, membre);
                rapportTaches.add(rapport);
            }
        }

        rapportTacheRepository.saveAll(rapportTaches);
        logger.info("Importation des rapports terminée avec succès.");
    }

    public void importerTachesOutild(Path cheminCSV) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(cheminCSV);
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : parser) {
                Long tacheId = Long.parseLong(record.get("Tâche ID"));
                Long outilId = Long.parseLong(record.get("Outil ID"));

                Tache tache = tacheRepository.findById(tacheId)
                        .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable : " + tacheId));
                Outil outil = outilRepository.findById(outilId)
                        .orElseThrow(() -> new EntityNotFoundException("Outil introuvable : " + outilId));

                if (tache.getOutils() == null) {
                    tache.setOutils(new ArrayList<>());
                }

                tache.getOutils().add(outil);
            }
        }

        logger.info("Mise à jour des outils liés aux tâches...");
        tacheRepository.findAll().forEach(tacheRepository::save);
    }
}