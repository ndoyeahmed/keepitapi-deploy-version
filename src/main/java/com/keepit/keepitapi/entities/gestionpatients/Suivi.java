package com.keepit.keepitapi.entities.gestionpatients;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.keepit.keepitapi.entities.admin.Medecin;
import com.keepit.keepitapi.entities.admin.Patient;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
public class Suivi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Timestamp date;

    @ManyToOne
    @JoinColumn(name = "patient", referencedColumnName = "id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "medecin", referencedColumnName = "id")
    private Medecin medecin;

    @OneToMany(mappedBy = "suivi")
    @JsonIgnore
    private List<HistoriqueConstante> historiqueConstantes;
}
