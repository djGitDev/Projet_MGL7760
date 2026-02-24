package com.example.projet3.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projet3.model.EvaluationTache;
import com.example.projet3.repository.EvaluationTacheRepository;

@RestController
@RequestMapping("/api/evaluations")

public class EvaluationController {

    private final EvaluationTacheRepository evaluationTacheRepository;

    public EvaluationController(EvaluationTacheRepository evaluationTacheRepository) {
        this.evaluationTacheRepository = evaluationTacheRepository;
    }

    @GetMapping
    public List<EvaluationTache> getEvaluationTaches() {
        return evaluationTacheRepository.findAll();
    }

}
