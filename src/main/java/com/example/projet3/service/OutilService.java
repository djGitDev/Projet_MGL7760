package com.example.projet3.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.projet3.model.Outil;
import com.example.projet3.repository.OutilRepository;

@Service
public class OutilService {

    private OutilRepository outilRepository;

    // chercher un outil par son id

    public Outil getOutilById(Long id) {
        return outilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Outil non trouvé"));
    }

    // liste des outils disponnibles

    public List<Outil> getOutilsDisponibles() {
        return outilRepository.findByDisponibilite("Disponible");
    }

}
