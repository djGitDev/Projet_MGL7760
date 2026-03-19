package com.example.projet3.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.projet3.model.Outil;
import com.example.projet3.repository.OutilRepository;

@Service
public class OutilService {

    private OutilRepository outilRepository;

    // chercher un outil par son id

    public Outil getOutilById(Long id) {
        return outilRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Outil non trouvé"));
    }

    // liste des outils disponnibles

    public List<Outil> getOutilsDisponibles() {
        return outilRepository.findByDisponibilite("Disponible");
    }

}
