package com.example.projet3.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Enumerated(EnumType.STRING)
    private TypeTache type;

    @Enumerated(EnumType.STRING)
    private EtatTache etat;

    private String description;

    // Relation Many‑to‑Many vers Membre
    @ManyToMany
    @JoinTable(name = "tache_membre", joinColumns = @JoinColumn(name = "tache_id"), inverseJoinColumns = @JoinColumn(name = "membre_id"))
    private List<Membre> membres = new ArrayList<>();

    private int dureeEstimee;

    @OneToMany(mappedBy = "tache", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EvaluationTache> evaluations = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    public void setEvaluations(List<EvaluationTache> evaluations) {
        this.evaluations = evaluations;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE })

    @JoinTable(name = "tache_outil", joinColumns = @JoinColumn(name = "tache_id"), inverseJoinColumns = @JoinColumn(name = "outil_id"))
    private List<Outil> outils = new ArrayList<>();

    // attribut pour l'ordre de séquence
    private int ordre;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordre")
    @JsonManagedReference
    private List<Tache> enfants = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private Tache parent;

    // Constructeurs, getters et setters

    public Tache(String nom, TypeTache type, String description, int dureeEstimee, Organisation organisation,
            int ordre) {
        this.nom = nom;
        this.ordre = ordre;
        this.type = type;
        this.etat = EtatTache.PLANNED;
        this.description = description;
        this.dureeEstimee = dureeEstimee;
        this.organisation = organisation;
    }

    // Getters et Setters
    public void setOutils(List<Outil> outils) {
        this.outils = outils;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Tache tache = (Tache) o;
        return id.equals(tache.id);
    }

    public Long getId() {
        return id;
    }

    public int getOrdre() {
        return ordre;
    }

    public Tache getParent() {
        return parent;
    }

    public List<Tache> getEnfants() {
        return enfants;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public void setParent(Tache parent) {
        this.parent = parent;
    }

    public void setEnfants(List<Tache> enfants) {
        this.enfants = enfants;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setType(TypeTache type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDureeEstimee(int dureeEstimee) {
        this.dureeEstimee = dureeEstimee;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public String getNom() {
        return nom;
    }

    public TypeTache getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public int getDureeEstimee() {
        return dureeEstimee;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Tache() {
    }

    public List<Outil> getOutils() {
        return outils;
    }

    public EtatTache getEtat() {
        return etat;
    }

    public void removeOutil(Outil outil) {
        outils.remove(outil);
        outil.getTaches().remove(this);
    }

    public void addOutil(Outil outil) {
        outils.add(outil);
        outil.getTaches().add(this);
    }

    public List<Membre> getMembres() {
        return membres;
    }

    public void setMembres(List<Membre> membres) {
        this.membres = membres;
    }

    public void addEnfant(Tache enfant) {
        enfant.setParent(this);
        enfants.add(enfant);
    }

    public void removeEnfant(Tache enfant) {
        enfants.remove(enfant);
        enfant.setParent(null);
    }

    // exemple de changement d'état
    public void setEtat(EtatTache nouvelEtat) {
        this.etat = nouvelEtat;

        if (nouvelEtat == EtatTache.DONE) {
            // 1) Propagation vers les enfants
            for (Tache e : enfants) {
                e.setEtat(EtatTache.DONE);
            }
            // 2) Puis remonte pour activer la suivante et/ou terminer le parent
            checkAndActivateNext();
        } else if (nouvelEtat == EtatTache.IN_PROGRESS) {
            // si on démarre cette tâche, mettre le parent en cours aussi
            if (parent != null && parent.getEtat() == EtatTache.PLANNED) {
                parent.setEtat(EtatTache.IN_PROGRESS);
            }
        }
    }

    // Si toutes les sous‑tâches sont DONE, on va marquer la suivante IN_PROGRESS

    private void checkAndActivateNext() {
        if (parent == null)
            return;

        List<Tache> taches = parent.getEnfants();
        int idx = taches.indexOf(this);

        // activer la tâche d’après dans la même liste
        if (idx >= 0 && idx + 1 < taches.size()) {
            Tache next = taches.get(idx + 1);
            if (next.getEtat() == EtatTache.PLANNED) {
                next.setEtat(EtatTache.IN_PROGRESS);
            }
        }

        // si tous les frères (les sous taches de ce parent) sont DONE, terminer le
        // parent
        boolean tousDone = taches.stream()
                .allMatch(t -> t.getEtat() == EtatTache.DONE);

        if (tousDone && parent.getEtat() != EtatTache.DONE) {
            parent.setEtat(EtatTache.DONE);
        }
    }

    @Override
    public String toString() {
        String result = "Tâche [id=" + id +
                ", nom=" + nom +
                ", type=" + type +
                ", etat=" + etat +
                ", description=" + description +
                ", ordre=" + ordre;

        // Ajout de l'id du parent si existant
        if (parent != null) {
            result += ", parentId=" + parent.getId();
        }

        // Ajout des ids des enfants si existants
        if (enfants != null && !enfants.isEmpty()) {
            result += ", enfantsIds=[";
            for (int i = 0; i < enfants.size(); i++) {
                result += enfants.get(i).getId();
                if (i < enfants.size() - 1) {
                    result += ", ";
                }
            }
            result += "]";
        }

        result += "]";
        return result;
    }

    public List<EvaluationTache> getEvaluations() {
        return evaluations;

    }

    public void addEvaluation(EvaluationTache eval) {
        evaluations.add(eval);
        eval.setTache(this);
    }
}
