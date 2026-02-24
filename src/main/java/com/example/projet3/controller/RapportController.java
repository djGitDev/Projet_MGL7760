package com.example.projet3.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.projet3.model.RapportTache;
import com.example.projet3.repository.RapportTacheRepository;

@RestController
@RequestMapping("/api/rapports")
public class RapportController {

    private final RapportTacheRepository rapportTacheRepository;

    public RapportController(RapportTacheRepository rapportTacheRepository) {
        this.rapportTacheRepository = rapportTacheRepository;
    }

    @GetMapping
    public List<RapportTache> getRapportTaches() {
        return rapportTacheRepository.findAll();
    }
}
