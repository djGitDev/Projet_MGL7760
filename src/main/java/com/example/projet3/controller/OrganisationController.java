package com.example.projet3.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projet3.model.Organisation;
import com.example.projet3.repository.OrganisationRepository;

@RestController
@RequestMapping("/api/organisations")
public class OrganisationController {

    private final OrganisationRepository organisationRepository;

    public OrganisationController(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    @GetMapping
    public List<Organisation> getOrganisations() {
        return organisationRepository.findAll();
    }
}
