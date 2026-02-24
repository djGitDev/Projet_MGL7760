package com.example.projet3.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.projet3.model.Outil;
import com.example.projet3.repository.OutilRepository;

@RestController
@RequestMapping("/api/outils")
public class OutilController {

    private final OutilRepository outilRepository;

    public OutilController(OutilRepository outilRepository) {
        this.outilRepository = outilRepository;
    }

    @GetMapping
    public List<Outil> getOutils() {
        return outilRepository.findAll();
    }
}
