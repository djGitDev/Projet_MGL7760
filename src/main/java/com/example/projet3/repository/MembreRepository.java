package com.example.projet3.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.projet3.model.Membre;

@Repository
public interface MembreRepository extends JpaRepository<Membre, Long> {

    // retourner un membre a partir id d'une organisation
    Optional<Membre> findByOrganisationId(Long organisationId);

    @Query("SELECT m.organisation.id FROM Membre m WHERE m.id = :id")
    Long findOrganisation_IdById(@Param("id") Long id);

    // liste des membres assignés à une tâche
    List<Membre> findByTaches_Id(Long tacheId);

    // Compte le nombre de membres rattachés à une organisation donnée.

    @Query("SELECT COUNT(m) FROM Membre m WHERE m.organisation.id = :orgId")
    long countMembresByOrganisation(@Param("orgId") Long organisationId);
}