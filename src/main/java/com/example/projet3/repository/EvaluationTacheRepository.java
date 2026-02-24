package com.example.projet3.repository;

import com.example.projet3.model.EvaluationTache;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationTacheRepository extends JpaRepository<EvaluationTache, Long> {

    List<EvaluationTache> findByMembreId(Long membreId);

    // Requête JPQL pour sommer les scores des évaluations pour un membre
    @Query("SELECT COALESCE(SUM(e.score), 0) FROM EvaluationTache e WHERE e.membre.id = :membreId")
    int sumScoreByMembreId(@Param("membreId") Long membreId);

    @Query("SELECT COALESCE(SUM(e.score), 0) FROM EvaluationTache e WHERE e.membre.organisation.id = :organisationId")
    int sumScoreByOrganisationId(@Param("organisationId") Long organisationId);

    List<EvaluationTache> findByTacheId(Long tacheId);
}
