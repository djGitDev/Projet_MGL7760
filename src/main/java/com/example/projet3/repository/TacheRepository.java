package com.example.projet3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.projet3.model.Tache;

@Repository

public interface TacheRepository extends JpaRepository<Tache, Long> {

    // liste des tâches auxquelles participe un membre
    List<Tache> findByMembres_Id(Long membreId);

    List<Tache> findByOrganisationId(Long organisationId);

    long countByOrganisationId(Long organisationId);
}
