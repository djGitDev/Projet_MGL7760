package com.example.projet3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.projet3.model.Outil;

@Repository
public interface OutilRepository extends JpaRepository<Outil, Long> {

    List<Outil> findByDisponibilite(String disponibilite);

    // retourner liste outils par type
    List<Outil> findByType(String type);

    List<Outil> findByTachesId(Long tacheId);
}
