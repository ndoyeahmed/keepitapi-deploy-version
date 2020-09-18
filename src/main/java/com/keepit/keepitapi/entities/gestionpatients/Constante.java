package com.keepit.keepitapi.entities.gestionpatients;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Constante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String libelle;
    private Double seuil;
    @Column(columnDefinition = "boolean default false")
    protected boolean archive;

    @OneToMany(mappedBy = "constante")
    @JsonIgnore
    private List<HistoriqueConstante> historiqueConstantes;
}
