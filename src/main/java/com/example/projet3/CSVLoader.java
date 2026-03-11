package com.example.projet3;

import com.example.projet3.service.CSVService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CSVLoader implements CommandLineRunner {

        private static final Logger logger = LoggerFactory.getLogger(CSVLoader.class);
        
        private final DataSource dataSource;

        private final CSVService csvService;

        public CSVLoader(CSVService csvService, DataSource dataSource) {
                this.csvService = csvService;
                this.dataSource = dataSource;
        }

        @Override
        public void run(String... args) throws Exception {

                try (Connection conn = dataSource.getConnection();
                                Statement stmt = conn.createStatement()) {
                        stmt.execute("ALTER TABLE TACHE ALTER COLUMN ID RESTART WITH 101");
                        logger.info("ID auto-increment TACHE démarre à 101.");
                }
                try {
                        Path cheminOrganisations = Paths
                                        .get("C:\\Users\\user\\Desktop\\projet_3_spring_boot\\projet3\\src\\main\\resources\\data\\Organisation.csv");

                        Path cheminMembres = Paths
                                        .get("C:\\Users\\user\\Desktop\\projet_3_spring_boot\\projet3\\src\\main\\resources\\data\\members.csv");

                        Path cheminTaches = Paths
                                        .get("C:\\Users\\user\\Desktop\\projet_3_spring_boot\\projet3\\src\\main\\resources\\data\\Tâche.csv");

                        Path cheminOutils = Paths
                                        .get("C:\\Users\\user\\Desktop\\projet_3_spring_boot\\projet3\\src\\main\\resources\\data\\Outils.csv");
                        Path cheminEvaluations = Paths
                                        .get("C:\\Users\\user\\Desktop\\projet_3_spring_boot\\projet3\\src\\main\\resources\\data\\Evaluation_Tâches.csv");
                        Path cheminRapports = Paths
                                        .get("C:\\Users\\user\\Desktop\\projet_3_spring_boot\\projet3\\src\\main\\resources\\data\\Rapport_Tâches.csv");
                        Path cheminTacheOutils = Paths
                                        .get("C:\\Users\\user\\Desktop\\projet_3_spring_boot\\projet3\\src\\main\\resources\\data\\Outils_assignés_aux_tâches.csv");

                        csvService.importerOrganisations(cheminOrganisations);
                        csvService.importerMembres(cheminMembres);
                        csvService.importerTaches(cheminTaches);
                        csvService.importerOutils(cheminOutils);
                        csvService.importerEvaluations(cheminEvaluations);
                        csvService.importerRapports(cheminRapports);
                        csvService.importerTachesOutild(cheminTacheOutils);
                } catch (Exception e) {
                       logger.error("Erreur d'importation", e);
                }
        }
}
