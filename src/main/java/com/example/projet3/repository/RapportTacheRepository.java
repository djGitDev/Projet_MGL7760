package com.example.projet3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.projet3.model.EvaluationTache;
import com.example.projet3.model.RapportTache;

@Repository
public interface RapportTacheRepository extends JpaRepository<RapportTache, Long> {

}
