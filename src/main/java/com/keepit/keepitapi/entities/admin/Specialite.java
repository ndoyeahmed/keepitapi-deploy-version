package com.keepit.keepitapi.entities.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Specialite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String libelle;
    @Column(columnDefinition = "boolean default false")
    protected boolean archive;

    @OneToMany(mappedBy = "specialite")
    @JsonIgnore
    private List<Medecin> medecins;
}
