package com.keepit.keepitapi.entities.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Profil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String libelle;
    private String description;

    @OneToMany(mappedBy = "profil")
    @JsonIgnore
    private List<Utilisateur> utilisateurs;
}
