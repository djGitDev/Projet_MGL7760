
package com.example.projet3.service;

import com.example.projet3.model.Membre;
import com.example.projet3.model.TypeMembre;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class Authorisation {

    private MembreService membreService;

    public void verifierType(Long membreId, TypeMembre typeRequis,MembreService membreService) {
        this.membreService = membreService;
        Membre membre = membreService.getMembreById(membreId);
        // verifier si le membre existe
        if (membre == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Membre introuvable");
        }
        // verfier si le role donnée parmi les role authorisés
        if (membre.getType() != typeRequis) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    String.format("Accès refusé : Type requis = %s, mais votre rôle est %s", typeRequis,
                            membre.getType()));
        }
    }

    // verfier si le role donnée parmi les role authorisés

    public void verifierUnDesTypes(Long membreId, List<TypeMembre> typesAutorises) {
        Membre membre = membreService.getMembreById(membreId);
        if (membre == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Membre introuvable");
        }
        if (!typesAutorises.contains(membre.getType())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    String.format("Accès refusé : Votre rôle (%s) n'est pas autorisé. Rôles autorisés : %s",
                            membre.getType(), typesAutorises));
        }
    }

    public void verifierAdmin(Long membreId) {
        verifierType(membreId, TypeMembre.ADMIN,membreService);
    }

    public void verifierMember(Long membreId) {
        verifierUnDesTypes(membreId, List.of(TypeMembre.EMPLOYE, TypeMembre.VOLONTAIRE));

    }
}
