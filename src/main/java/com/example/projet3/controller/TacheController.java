package com.example.projet3.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.projet3.model.Tache;
import com.example.projet3.repository.TacheRepository;

@RestController
@RequestMapping("/api/taches")
public class TacheController {

    private final TacheRepository tacheRepository;

    public TacheController(TacheRepository tacheRepository) {
        this.tacheRepository = tacheRepository;
    }

    @GetMapping
    public List<Tache> getTaches() {
        return tacheRepository.findAll();
    }
}
