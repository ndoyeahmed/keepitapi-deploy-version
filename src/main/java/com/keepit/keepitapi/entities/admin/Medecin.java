package com.keepit.keepitapi.entities.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.keepit.keepitapi.entities.gestionpatients.Suivi;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Medecin extends Utilisateur {
    private String adresseCabinet;
    private String nomCabinet;
    private String telephoneCabinet;

    @ManyToOne
    @JoinColumn(name = "specialite", referencedColumnName = "id")
    private Specialite specialite;

    @OneToMany(mappedBy = "medecin")
    @JsonIgnore
    private List<Suivi> suivis;
}
