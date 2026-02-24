package com.example.projet3.repository;

import com.example.projet3.model.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Long> {

    @Query("SELECT o FROM Organisation o JOIN o.membres m WHERE m.id = :membreId")
    Optional<Organisation> findByMembreId(@Param("membreId") Long membreId);
}
