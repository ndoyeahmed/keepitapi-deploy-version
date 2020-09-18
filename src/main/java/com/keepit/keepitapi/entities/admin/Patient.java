package com.keepit.keepitapi.entities.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.keepit.keepitapi.entities.gestionpatients.Suivi;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Patient extends Utilisateur {
    private String cin;
    private Date dateNaissance;
    private String groupeSanguin;
    private String telephoneUrgence1;
    private String telephoneUrgence2;
    private String telephoneUrgence3;

    @OneToMany(mappedBy = "patient")
    @JsonIgnore
    private List<Suivi> suivis;
}
