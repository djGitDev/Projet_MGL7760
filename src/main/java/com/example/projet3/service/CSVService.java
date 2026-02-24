package com.example.projet3.service;

import com.example.projet3.model.*;
import com.example.projet3.repository.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private OrganisationRepository organisationRepository;
    @Autowired
    private MembreRepository membreRepository;
    @Autowired
    private TacheRepository tacheRepository;
    @Autowired
    private OutilRepository outilRepository;
    @Autowired
    private EvaluationTacheRepository evaluationTacheRepository;
    @Autowired
    private RapportTacheRepository rapportTacheRepository;

    // Méthode pour lire les tâches depuis un fichier CSV
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

                // Récupérer l'organisation correspondante
                Organisation organisation = organisationRepository.findById(organisationId).orElseThrow(
                        () -> new IllegalArgumentException("Organisation non trouvée avec l'ID : " + organisationId));
                // Récupérer l'ordre à partir de la séquence
                int ordre = Sequence.fromLibelle(nom) // Cherche la séquence correspondant au nom de la tâche
                        .map(Sequence::ordinal) // Récupère l'index de la séquence
                        .map(i -> i + 1) // Ajoute 1 pour avoir un ordre commençant à 1
                        .orElse(0);
                taches.add(new Tache(nom, type, description, dureeEstimee, organisation, ordre));
            }
        }
        // Enregistrer les taches en base de données
        tacheRepository.saveAll(taches);
        System.out.println("✅ Importation des taches terminée avec succès !");
    }

    // Méthode pour lire les organisations depuis un fichier CSV
    public void importerOrganisations(Path cheminFichier) throws IOException {
        List<Organisation> organisations = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(cheminFichier);
                CSVParser csvParser = new CSVParser(reader,
                        CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            for (CSVRecord record : csvParser) {

                String nom = record.get("Nom de l'Organisation");
                String type = record.get("Type d'Organisation");

                organisations.add(new Organisation(nom, type));
            }
        }

        // Enregistrer les organisations en base de données
        organisationRepository.saveAll(organisations);
        System.out.println("✅ Importation des organisations terminée avec succès !");
    }

    // Méthode pour lire les membres depuis un fichier CSV

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

                // Récupérer l'organisation correspondante
                Organisation organisation = organisationRepository.findById(organisationId).orElseThrow(
                        () -> new IllegalArgumentException("Organisation non trouvée avec l'ID : " + organisationId));

                membres.add(new Membre(nom, prenom, type, organisation, dateAdhesion));
            }
        }
        // Enregistrer les membres en base de données
        membreRepository.saveAll(membres);
        System.out.println("✅ Importation des membres terminée avec succès !");
    }

    public void importerOutils(Path cheminFichier) throws IOException {
        List<Outil> outils = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(cheminFichier);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {
            for (CSVRecord record : csvParser) {
                String nom = record.get("Nom de l'Outil");
                String type = record.get("Type");
                Long organisationId = Long.parseLong(record.get("Organisation ID"));
                String disponibilité = record.get("Disponibilité");
                String dateAcht = record.get("Date d'Achat");
                int nombre = Integer.parseInt(record.get("Nombre d'Utilisations"));

                // Récupérer l'organisation correspondante
                Organisation organisation = organisationRepository.findById(organisationId).orElseThrow(
                        () -> new IllegalArgumentException("Organisation non trouvée avec l'ID : " + organisationId));
                outils.add(new Outil(nom, type, disponibilité, dateAcht, nombre, organisation));
            }
        }
        // Enregistrer les membres en base de données
        outilRepository.saveAll(outils);
        System.out.println("✅ Importation des outils terminée avec succès !");

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
                        .orElseThrow(() -> new RuntimeException("Tâche introuvable : " + tacheId));
                Membre membre = membreRepository.findById(membreId)
                        .orElseThrow(() -> new RuntimeException("Membre introuvable : " + membreId));

                EvaluationTache evaluation = new EvaluationTache(score, commentaire, tache, membre);
                evaluationTaches.add(evaluation);
            }
        }
        evaluationTacheRepository.saveAll(evaluationTaches);
        System.out.println("✅ Importation des evalustions terminée avec succès !");
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
                        .orElseThrow(() -> new RuntimeException("Tâche introuvable : " + tacheId));
                Membre membre = membreRepository.findById(membreId)
                        .orElseThrow(() -> new RuntimeException("Membre introuvable : " + membreId));

                RapportTache rapport = new RapportTache(date, commentaire, etat, tache, membre);
                rapportTaches.add(rapport);

            }
        }

        rapportTacheRepository.saveAll(rapportTaches);
        System.out.println("✅ Importation des rapports terminée avec succès !");
    }

    public void importerTachesOutild(Path cheminCSV) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(cheminCSV);
                CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : parser) {
                Long tacheId = Long.parseLong(record.get("Tâche ID"));
                Long outilId = Long.parseLong(record.get("Outil ID"));

                Tache tache = tacheRepository.findById(tacheId)
                        .orElseThrow(() -> new RuntimeException("Tâche introuvable : " + tacheId));
                Outil outil = outilRepository.findById(outilId)
                        .orElseThrow(() -> new RuntimeException("Outil introuvable : " + outilId));

                // On vérifie que la liste des outils n'est pas null
                if (tache.getOutils() == null) {
                    tache.setOutils(new ArrayList<>());
                }

                // Ajouter l'outil à la tâche
                tache.getOutils().add(outil);
            }
        }

        System.out.println("✅ Mise à jour des outils liée aux tâches...");
        // Sauvegarde globale : toutes les tâches sont mises à jour
        tacheRepository.findAll().forEach(tacheRepository::save);
    }
}